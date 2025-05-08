package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiDragDropFlags;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiTableFlags;
import io.github.ClassSyncCSS.ClassSync.Domain.*;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Locale;

public class Calendar {
    TimeSlot[] timeIntervals = {
            TimeSlot.EightToTen, TimeSlot.TenToTwelve, TimeSlot.TwelveToFourteen, TimeSlot.FourteenToSixteen, TimeSlot.SixteenToEighteen, TimeSlot.EighteenToTwenty,
    };
    TimeTableSlot[][] scheduleData;

    private SidePane sidePaneRef;

    public Calendar() {
        scheduleData = new TimeTableSlot[timeIntervals.length][Weekday.values().length];
        for (int i = 0; i < scheduleData.length; i++) {
            for (int j = 0; j < scheduleData[i].length; j++) {
                scheduleData[i][j] = null;
            }
        }

        // Initialize the scheduleData with some dummy data
        scheduleData[0][0] = new TimeTableSlot(Weekday.Monday, timeIntervals[0], null, null, null, new Discipline(), ActivityType.Course);
        scheduleData[0][1] = new TimeTableSlot(Weekday.Monday, timeIntervals[0], null, null, null, new Discipline(), ActivityType.Lab);
    }

    public void setSidePaneRef(SidePane pane) {
        this.sidePaneRef = pane;
    }

    public static final String PAYLOAD_TYPE_SCHEDULE_CELL = "SCHEDULE_CELL";

    public void process() {
        ImGui.begin("Calendar");

        float height = ImGui.getWindowHeight() * (0.945f / timeIntervals.length);

        if (ImGui.beginTable("test", 6, ImGuiTableFlags.Borders)) {
            ImGui.tableSetupColumn("Time");
            for (Weekday weekday : Weekday.values()) {
                ImGui.tableSetupColumn(weekday.name());
            }
            ImGui.tableHeadersRow();

            for (int row = 0; row < timeIntervals.length; row++) {
                ImGui.tableNextRow(height);

                ImGui.tableSetColumnIndex(0);
                ImGui.text(String.format("%02d:00 - %02d:00", timeIntervals[row].getStart(), timeIntervals[row].getEnd()));

                for (int column = 0; column < Weekday.values().length; column++) {
                    ImGui.tableSetColumnIndex(column + 1); // Move to the correct column (offset by 1)

                    TimeTableSlot cellContent = scheduleData[row][column];

                    String selectableId = "##ScheduleCell_" + row + "_" + column;


                    ImGui.selectable(selectableId, false, ImGuiSelectableFlags.AllowItemOverlap,
                                    new ImVec2(ImGui.getColumnWidth(column + 1), height - 2));


                    if (cellContent != null &&  ImGui.beginDragDropSource()) {
                        ImGui.setDragDropPayload(PAYLOAD_TYPE_SCHEDULE_CELL, cellContent);

                        ImGui.text("Dragging...");
                        ImGui.endDragDropSource();
                    }

                    if (ImGui.beginDragDropTarget()) {
                        TimeTableSlot receivedPayload = ImGui.acceptDragDropPayload(PAYLOAD_TYPE_SCHEDULE_CELL, ImGuiDragDropFlags.None);

                        if (receivedPayload != null) {
                            var sourceWeekday = receivedPayload.getWeekday();
                            var sourceTimeSlot = receivedPayload.getSlot();

                            if(sourceWeekday != null && sourceTimeSlot != null) {
                                // We have to swap
                                int sourceRow = Arrays.stream(timeIntervals).toList().indexOf(sourceTimeSlot);
                                int sourceCol = sourceWeekday.ordinal();

                                int targetRow = row;
                                int targetCol = column;
                                Weekday targetWeekday = Weekday.values()[targetCol];
                                TimeSlot targetTimeSlot = timeIntervals[targetRow];

                                // Swap the content
                                TimeTableSlot temp = scheduleData[targetRow][targetCol];
                                scheduleData[targetRow][targetCol] = scheduleData[sourceRow][sourceCol];
                                scheduleData[sourceRow][sourceCol] = temp;
                                // Update the source cell
                                if(scheduleData[sourceRow][sourceCol] != null) {
                                    scheduleData[sourceRow][sourceCol].setWeekday(sourceWeekday);
                                    scheduleData[sourceRow][sourceCol].setSlot(sourceTimeSlot);
                                }

                                // Update the target cell
                                if(scheduleData[targetRow][targetCol] != null) {
                                    scheduleData[targetRow][targetCol].setWeekday(targetWeekday);
                                    scheduleData[targetRow][targetCol].setSlot(targetTimeSlot);
                                }

                            } else {
                                // We just add it, we come from SidePane
                                this.sidePaneRef.slots.remove(receivedPayload);
                                scheduleData[row][column] = receivedPayload;
                                scheduleData[row][column].setWeekday(Weekday.values()[column]);
                                scheduleData[row][column].setSlot(timeIntervals[row]);
                            }

//                            System.out.println("Dropped content from [" + source[0] + "," + source[1] + "] to [" + row + "," + column + "]");
                        }
                        ImGui.endDragDropTarget(); // End the drag target
                    }

                    ImGui.sameLine(5, 0);
                    ImGui.setCursorPosX(ImGui.getCursorPosX() + ImGui.getStyle().getFramePaddingX());

                    ImGui.beginGroup();
                        if(cellContent == null) {
                            ImGui.text("empty");
                        } else {
                            ImGui.text(String.format("%s - %s", cellContent.getDiscipline().getName(), cellContent.getActivityType().name()));
//                            ImGui.beginCombo("##tt_prof", );
                        }
                    ImGui.endGroup();
                }
            }

            ImGui.endTable();
        }

        ImGui.end();
    }
}
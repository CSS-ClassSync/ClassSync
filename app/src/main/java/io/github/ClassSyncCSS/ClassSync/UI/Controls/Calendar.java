package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiDragDropFlags;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiTableFlags;
import io.github.ClassSyncCSS.ClassSync.Domain.TimeSlot;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Calendar {
    TimeSlot[] timeIntervals = {
            TimeSlot.EightToTen, TimeSlot.TenToTwelve, TimeSlot.TwelveToFourteen, TimeSlot.FourteenToSixteen, TimeSlot.SixteenToEighteen, TimeSlot.EighteenToTwenty,
    };
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h a", Locale.ENGLISH); // e.g., "8 AM"
    String[] workingDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    String[][] scheduleData;

    public Calendar() {
        scheduleData = new String[timeIntervals.length][workingDays.length];
        for (int i = 0; i < scheduleData.length; i++) {
            for (int j = 0; j < scheduleData[i].length; j++) {
                scheduleData[i][j] = String.format("Content [%d,%d]", i, j);
            }
        }
    }

    private static final String PAYLOAD_TYPE_SCHEDULE_CELL = "SCHEDULE_CELL";

    public void process() {
        ImGui.begin("Calendar");

        float height = ImGui.getWindowHeight() * (0.945f / timeIntervals.length);

        if (ImGui.beginTable("test", 6, ImGuiTableFlags.Borders)) {
            ImGui.tableSetupColumn("Time");
            for (String weekday : workingDays) {
                ImGui.tableSetupColumn(weekday);
            }
            ImGui.tableHeadersRow();

            for (int row = 0; row < timeIntervals.length; row++) {
                ImGui.tableNextRow(height);

                ImGui.tableSetColumnIndex(0);
                ImGui.text(String.format("%02d:00 - %02d:00", timeIntervals[row].start, timeIntervals[row].end));

                for (int column = 0; column < workingDays.length; column++) {
                    ImGui.tableSetColumnIndex(column + 1); // Move to the correct column (offset by 1)

                    String cellContent = scheduleData[row][column];

                    String selectableId = "##ScheduleCell_" + row + "_" + column;


                    ImGui.selectable(selectableId, false, ImGuiSelectableFlags.AllowItemOverlap,
                                    new ImVec2(ImGui.getColumnWidth(column + 1), height - 2));


                    if (ImGui.beginDragDropSource()) {
                        String payloadData = row + "," + column;
                        ImGui.setDragDropPayload(PAYLOAD_TYPE_SCHEDULE_CELL, payloadData);

                        ImGui.text("Dragging: " + cellContent);
                        ImGui.endDragDropSource();
                    }

                    if (ImGui.beginDragDropTarget()) {
                        String receivedPayload = ImGui.acceptDragDropPayload(PAYLOAD_TYPE_SCHEDULE_CELL, ImGuiDragDropFlags.None);

                        if (receivedPayload != null) {
                            var source = receivedPayload.split(",");
                            int sourceRow = Integer.parseInt(source[0]);
                            int sourceCol = Integer.parseInt(source[1]);

                            var dest_table_val = scheduleData[row][column];
                            scheduleData[row][column] = scheduleData[sourceRow][sourceCol];
                            scheduleData[sourceRow][sourceCol] = dest_table_val;

                            System.out.println("Dropped content from [" + source[0] + "," + source[1] + "] to [" + row + "," + column + "]");
                        }
                        ImGui.endDragDropTarget(); // End the drag target
                    }

                    ImGui.sameLine(5, 0);
                    ImGui.setCursorPosX(ImGui.getCursorPosX() + ImGui.getStyle().getFramePaddingX());

                    ImGui.beginGroup();
                        ImGui.text(cellContent);
                        ImGui.text("Line 2");
                    ImGui.endGroup();
                }
            }

            ImGui.endTable();
        }

        ImGui.end();
    }
}
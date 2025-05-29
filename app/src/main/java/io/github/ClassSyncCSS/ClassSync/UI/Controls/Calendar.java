package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiDragDropFlags;
import imgui.flag.ImGuiSelectableFlags;
import imgui.flag.ImGuiTableFlags;
import io.github.ClassSyncCSS.ClassSync.Domain.*;

import java.util.*;

public class Calendar {
    final TimeSlot[] timeIntervals = {
            TimeSlot.EightToTen, TimeSlot.TenToTwelve, TimeSlot.TwelveToFourteen, TimeSlot.FourteenToSixteen, TimeSlot.SixteenToEighteen, TimeSlot.EighteenToTwenty,
    };
    TimeTable fullTimeTable;
    TimeTableSlot[][] scheduleData;

    private SidePane sidePaneRef;
    private Filters filtersRef;

    private boolean classInvariant() {
        return this.sidePaneRef != null && this.filtersRef != null &&
                fullTimeTable != null && scheduleData != null && scheduleData.length == timeIntervals.length &&
                Arrays.stream(scheduleData).allMatch(row -> row.length == Weekday.values().length);
    }

    public Calendar() {
        scheduleData = new TimeTableSlot[timeIntervals.length][Weekday.values().length];
        for (int i = 0; i < scheduleData.length; i++) {
            for (int j = 0; j < scheduleData[i].length; j++) {
                scheduleData[i][j] = null;
            }
        }
        fullTimeTable = new TimeTable();

        assert scheduleData != null && scheduleData.length == timeIntervals.length : "Schedule data must be initialized with the correct size.";
        assert Arrays.stream(scheduleData).allMatch(row -> row.length == Weekday.values().length) : "Each row in schedule data must have the same length as the number of weekdays.";
        assert fullTimeTable != null : "FullTimeTable must be initialized before use.";
    }

    public void setData(AllData data) {
        assert data != null : "Data must not be null.";

        fullTimeTable.setData(data);
    }

    public void setSidePaneRef(SidePane pane) {
        assert pane != null : "SidePane reference must not be null.";

        this.sidePaneRef = pane;
    }

    public void setFiltersRef(Filters filters) {
        assert filters != null : "Filters reference must not be null.";

        this.filtersRef = filters;
        this.filtersRef.setCalendarRef(this);
    }

    public static final String PAYLOAD_TYPE_SCHEDULE_CELL = "SCHEDULE_CELL";

    public void process() {
        ImGui.begin("Calendar");

        if (filtersRef.areFiltersValid()) {
            displayTable();
        } else {
            ImGui.textColored(0xfffc5f53, "The filters you have selected are too broad, please narrow your search.");
        }

        ImGui.end();
        
        assert classInvariant() : "Class invariant violated.";
    }

    public void updateFilters() {
        assert classInvariant() : "Class invariant violated before updating filters.";

        Professor selectedProf = filtersRef.getSelectedProfessor();
        Group selectedGroup = filtersRef.getSelectedGroup();
        Discipline selectedDiscipline = filtersRef.getSelectedDiscipline();
        Room selectedRoom = filtersRef.getSelectedRoom();

        // --- Side pane
        List<TimeTableSlot> classesRemaining = new ArrayList<>();

        if (selectedGroup != null) {
            classesRemaining.addAll(fullTimeTable.getClassesRemainingByGroup(selectedGroup));
        }
        if (selectedProf != null) {

            if (classesRemaining.isEmpty()) {
                // We're doing just this
                classesRemaining.addAll(fullTimeTable.getClassesRemainingByProfessor(selectedProf));
            } else {
                // We're multi-filtering
                classesRemaining.retainAll(fullTimeTable.getClassesRemainingByProfessor(selectedProf));
            }
        }
        if (selectedDiscipline != null) {
            if (classesRemaining.isEmpty()) {
                // We're doing just this
                classesRemaining.addAll(fullTimeTable.getClassesRemainingByDiscipline(selectedDiscipline));
            } else {
                // We're multi-filtering
                classesRemaining.retainAll(fullTimeTable.getClassesRemainingByDiscipline(selectedDiscipline));
            }
        }

        this.sidePaneRef.slots = classesRemaining;

        // --- Actual Calendar, scheduleData
        Map<Weekday, List<TimeTableSlot>> schedule = null;

        if (selectedGroup != null) {
            schedule = fullTimeTable.getScheduleByGroup(selectedGroup);
        } else if (selectedProf != null) {
            schedule = fullTimeTable.getScheduleByProfessor(selectedProf);
        } else if (selectedDiscipline != null) {
            schedule = fullTimeTable.getScheduleByDiscipline(selectedDiscipline);
        } else if (selectedRoom != null) {
            schedule = fullTimeTable.getScheduleByRoom(selectedRoom);
        }

        if (schedule != null) {
            for (int i = 0; i < timeIntervals.length; i++) {
                for (int j = 0; j < Weekday.values().length; j++) {
                    scheduleData[i][j] = null;
                }
            }

            for (Map.Entry<Weekday, List<TimeTableSlot>> entry : schedule.entrySet()) {
                Weekday weekday = entry.getKey();
                List<TimeTableSlot> slots = entry.getValue();

                for (TimeTableSlot slot : slots) {
                    int row = Arrays.stream(timeIntervals).toList().indexOf(slot.getSlot());
                    int column = weekday.ordinal();
                    scheduleData[row][column] = slot;
                }
            }
        }
        // If no filters are selected, reset the scheduleData
        else {
            for (int i = 0; i < timeIntervals.length; i++) {
                for (int j = 0; j < Weekday.values().length; j++) {
                    scheduleData[i][j] = null;
                }
            }
        }

        assert classInvariant() : "Class invariant violated after updating filters.";
    }

    private void displayTable() {
        assert classInvariant() : "Class invariant violated before displaying the table.";

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


                    if (cellContent != null && ImGui.beginDragDropSource()) {
                        ImGui.setDragDropPayload(PAYLOAD_TYPE_SCHEDULE_CELL, cellContent);

                        ImGui.text("Dragging...");
                        ImGui.endDragDropSource();
                    }

                    if (ImGui.beginDragDropTarget()) {
                        TimeTableSlot previewPayload = ImGui.getDragDropPayload(PAYLOAD_TYPE_SCHEDULE_CELL);
                        
                        if (previewPayload != null) {
                            // Create validation copy with target position
                            TimeTableSlot validationCopy = new TimeTableSlot(
                                Weekday.values()[column], 
                                timeIntervals[row],
                                previewPayload.getProfessor(),
                                previewPayload.getRoom(),
                                previewPayload.getGroup(),
                                previewPayload.getDiscipline(),
                                previewPayload.getActivityType()
                            );
                            
                            boolean canAccept = fullTimeTable.checkAdd(validationCopy);
                            
                            if (canAccept) {
                                TimeTableSlot receivedPayload = ImGui.acceptDragDropPayload(PAYLOAD_TYPE_SCHEDULE_CELL);
                                
                                if (receivedPayload != null) {
                                    var sourceWeekday = receivedPayload.getWeekday();
                                    var sourceTimeSlot = receivedPayload.getSlot();

                                    if (sourceWeekday != null && sourceTimeSlot != null) {
                                        // Coming from another cell in the Calendar
                                        int sourceRow = Arrays.stream(timeIntervals).toList().indexOf(sourceTimeSlot);
                                        int sourceCol = sourceWeekday.ordinal();

                                        int targetRow = row;
                                        int targetCol = column;

                                        // First, remove the slot from its current position in the TimeTable
                                        if (scheduleData[sourceRow][sourceCol] != null) {
                                            fullTimeTable.removeDiscipline(scheduleData[sourceRow][sourceCol]);
                                        }

                                        // Then update visual representation
                                        TimeTableSlot temp = scheduleData[targetRow][targetCol];
                                        scheduleData[targetRow][targetCol] = scheduleData[sourceRow][sourceCol];
                                        scheduleData[sourceRow][sourceCol] = temp;
                                        
                                        // Update the slots with their new positions
                                        if (scheduleData[sourceRow][sourceCol] != null) {
                                            scheduleData[sourceRow][sourceCol].setWeekday(sourceWeekday);
                                            scheduleData[sourceRow][sourceCol].setSlot(sourceTimeSlot);
                                            
                                            // Re-add the moved slot to TimeTable at its new position
                                            fullTimeTable.addDiscipline(scheduleData[sourceRow][sourceCol]);
                                        }

                                        if (scheduleData[targetRow][targetCol] != null) {
                                            scheduleData[targetRow][targetCol].setWeekday(Weekday.values()[column]);
                                            scheduleData[targetRow][targetCol].setSlot(timeIntervals[row]);
                                            
                                            // Add the target cell content to TimeTable
                                            fullTimeTable.addDiscipline(scheduleData[targetRow][targetCol]);
                                        }
                                    } else {
                                        // Coming from SidePane
                                        this.sidePaneRef.slots.remove(receivedPayload);
                                        
                                        // First, remove any existing slot at this position
                                        if (scheduleData[row][column] != null) {
                                            fullTimeTable.removeDiscipline(scheduleData[row][column]);
                                        }
                                        
                                        // Then update with the new slot
                                        receivedPayload.setWeekday(Weekday.values()[column]);
                                        receivedPayload.setSlot(timeIntervals[row]);

                                        scheduleData[row][column] = receivedPayload;
//                                        scheduleData[row][column].setWeekday(Weekday.values()[column]);
//                                        scheduleData[row][column].setSlot(timeIntervals[row]);
//
                                        // Add it to the fullTimeTable
                                        fullTimeTable.addDiscipline(receivedPayload);
                                    }
                                }
                            }
                        }
        
                        ImGui.endDragDropTarget();
                    }

                    ImGui.sameLine(5, 0);
                    ImGui.setCursorPosX(ImGui.getCursorPosX() + ImGui.getStyle().getFramePaddingX());

                    ImGui.beginGroup();
                    if (cellContent == null) {
                        ImGui.text("empty");
                    } else {
                        ImGui.text(String.format("%s - %s", cellContent.getDiscipline().getName(), cellContent.getActivityType().name()));
                        ImGui.text(String.format("Group: %s", cellContent.getGroup().getName()));
                    }
                    ImGui.endGroup();
                }
            }

            ImGui.endTable();
        }

        assert classInvariant() : "Class invariant violated after displaying the table.";
    }
}
package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiSelectableFlags;
import io.github.ClassSyncCSS.ClassSync.Domain.ActivityType;
import io.github.ClassSyncCSS.ClassSync.Domain.AllData;
import io.github.ClassSyncCSS.ClassSync.Domain.Discipline;
import io.github.ClassSyncCSS.ClassSync.Domain.TimeTableSlot;
import io.github.ClassSyncCSS.ClassSync.Domain.Professor;

import java.util.ArrayList;
import java.util.List;

public class SidePane {

    public List<TimeTableSlot> slots;


    public SidePane(Filters filters) {
        slots = new ArrayList<>();
        List<Discipline> disciplines = AllData.load().getDisciplines();
        Professor selectedProfessor = filters.getSelectedProfessor();

//        if (selectedProfessor != null) {
//            // Filter disciplines that include the selected professor
//            disciplines = disciplines.stream()
//                    .filter(d -> (d.getCourseProfs() != null && d.getCourseProfs().contains(selectedProfessor)) ||
//                            (d.getLaboratoryProfs() != null && d.getLaboratoryProfs().contains(selectedProfessor)))
//                    .toList();
//        }

        for (Discipline discipline : disciplines) {
            var slot = new TimeTableSlot(
                    null, null, null,
                    null, null,
                    discipline,
                    ActivityType.Course
            );
            slots.add(slot);
        }
    }

    public void process() {
        ImGui.begin("Classes");

        float width = ImGui.getWindowWidth();

        var iterator = slots.iterator();
        while (iterator.hasNext()) {
            TimeTableSlot slot = iterator.next();

            ImGui.selectable("##class_selectable", false, ImGuiSelectableFlags.AllowItemOverlap, new ImVec2(width, 100));

            if (slot != null &&  ImGui.beginDragDropSource()) {
                ImGui.setDragDropPayload(Calendar.PAYLOAD_TYPE_SCHEDULE_CELL, slot);
                ImGui.text("Dragging...");
                ImGui.endDragDropSource();
            }

            ImGui.sameLine(5, 0);
            ImGui.setCursorPosX(ImGui.getCursorPosX() + ImGui.getStyle().getFramePaddingX());
            ImGui.beginGroup();
            if(slot == null) {
                ImGui.text("empty");
            } else {
                ImGui.text(String.format("%s - %s", slot.getDiscipline().getName(), slot.getActivityType().name()));
            }
            ImGui.endGroup();

        }

        ImGui.end();
    }
}

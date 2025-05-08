package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiSelectableFlags;
import io.github.ClassSyncCSS.ClassSync.Domain.ActivityType;
import io.github.ClassSyncCSS.ClassSync.Domain.Discipline;
import io.github.ClassSyncCSS.ClassSync.Domain.TimeTableSlot;

import java.util.ArrayList;
import java.util.List;

public class SidePane {

    public List<TimeTableSlot> slots;


    public SidePane() {
        slots = new ArrayList<>();
//        var slot = new TimeTableSlot(null, null, null,
//                                    null, null,
//                                new Discipline("Test Discipline", null, null),
//                                ActivityType.Course);
//        slots.add(slot);
//        var slot2 = new TimeTableSlot(null, null, null,
//                                    null, null,
//                                new Discipline("Test Discipline", null, null),
//                                ActivityType.Lab);
//        slots.add(slot2);
    }

    public void process() {
        ImGui.begin("Classes");

        float width = ImGui.getWindowWidth();

        for(int i = 0; i < slots.size(); i++) {
            TimeTableSlot slot = slots.get(i);

            String selectableId = String.format("##class_selectable_%d", i);
            ImGui.selectable(selectableId, false, ImGuiSelectableFlags.AllowItemOverlap, new ImVec2(width, 100));

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
                ImGui.text(String.format("Group: %s", slot.getGroup().getName()));
            }
            

            ImGui.endGroup();

        }

        ImGui.end();
    }
}

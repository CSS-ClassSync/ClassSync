package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.flag.ImGuiSelectableFlags;
import imgui.type.ImInt;
import io.github.ClassSyncCSS.ClassSync.Domain.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SidePane {

    public List<TimeTableSlot> slots;
    AllData data;

    public void setAllData(AllData dat) {
        this.data = dat;
    }

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
            if(ImGui.selectable(selectableId, false, ImGuiSelectableFlags.AllowItemOverlap, new ImVec2(width, 100))) {
                System.out.println(slot.toString());
            }

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

            
            List<Professor> prof = slot.getDiscipline().getCourseProfs(); 
            List<String> profNames = prof.stream().map(s-> s.getName()).collect(Collectors.toUnmodifiableList());
            String[] profNamesRaw = profNames.toArray(new String[0]);

            ImInt selectedProf = new ImInt(
                    slot.getProfessor() != null ? slot.getDiscipline().getCourseProfs().indexOf(slot.getProfessor()):0
            );
            if(ImGui.combo("##Professor" + i, selectedProf, profNamesRaw)) {
                slot.setProfessor(prof.get(selectedProf.get()));
            }

            List<Room> roms = data.getRooms();
            List<String> roomNames = roms.stream().filter(r -> r.getType().contains(slot.getActivityType()))
                                         .map(r -> r.getName())
                                         .collect(Collectors.toUnmodifiableList());
            String[] roomNamesRaw = roomNames.toArray(new String[0]);
            ImInt selectedRoom = new ImInt(
                    slot.getRoom() != null ? roms.indexOf(slot.getRoom()):0
            );
            if(ImGui.combo("##Room" + i, selectedRoom, roomNamesRaw)) {
                slot.setRoom(roms.get(selectedRoom.get()));
            }

            ImGui.endGroup();

        }

        ImGui.end();
    }
}

package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import imgui.ImGui;
import imgui.ImGuiTextFilter;
import imgui.type.ImInt;
import imgui.type.ImString;

import java.util.List;

public class Filters {
    private List<String> professors;
    private ImInt selected = new ImInt();
    private ImString filter_text = new ImString();
    ImGuiTextFilter filter = new ImGuiTextFilter();


    public Filters(List<String> professors) {
        this.professors = professors;
    }

    public void process() {
        ImGui.begin("Filters");
            float window_width = ImGui.getWindowWidth();

            ImGui.setNextItemWidth(window_width * 0.3f);

            if(ImGui.beginCombo("##Professor", "Professor: " + professors.get(selected.get()))) {
                float width = ImGui.getWindowWidth();
                ImGui.setNextItemWidth(width - 10);

                if(ImGui.inputText("##ProfessorText", filter_text)) {
                    filter.setInputBuffer(filter_text.toString());
                    filter.build();
                }

                for (int i = 0; i < professors.size(); i++) {
                    boolean isSelected = (selected.get() == i);

                    // ImGui.selectable() returns true when the item is clicked
                    if (filter.passFilter(professors.get(i)) && ImGui.selectable(professors.get(i), isSelected)) {
                        selected.set(i); // Update the selected index
                    }

                    // Set the initial focus when opening the combo (scrolling to the selected item)
                    if (isSelected) {
                        ImGui.setItemDefaultFocus();
                    }
                }
                ImGui.endCombo();
            }
        ImGui.end();
    }
}

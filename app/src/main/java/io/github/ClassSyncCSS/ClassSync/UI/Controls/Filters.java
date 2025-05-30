package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import imgui.ImGui;
import imgui.ImGuiTextFilter;
import imgui.type.ImInt;
import imgui.type.ImString;
import io.github.ClassSyncCSS.ClassSync.Domain.*;

import java.util.ArrayList;
import java.util.List;

public class Filters {
    ImInt selectedProfessor = new ImInt();
    private ImString professorFilterInputText = new ImString();
    private ImGuiTextFilter professorFilter = new ImGuiTextFilter();

    ImInt selectedGroup = new ImInt();
    private ImString groupFilterInputText = new ImString();
    private ImGuiTextFilter groupFilter = new ImGuiTextFilter();

    ImInt selectedDiscipline = new ImInt();
    private ImString disciplineFilterInputText = new ImString();
    private ImGuiTextFilter disciplineFilter = new ImGuiTextFilter();

    private ImInt selectedRoom = new ImInt();
    private ImString roomFilterInputText = new ImString();
    private ImGuiTextFilter roomFilter = new ImGuiTextFilter();

    private AllData allData;

    private List<Professor> filteredProfessors;
    private List<Group> filteredGroups;
    private List<Discipline> filteredDisciplines;
    private List<Room> filteredRooms;

    private boolean _areFiltersValid = false;

    private Calendar calendarRef;

    public Filters(AllData allData) {
        assert allData != null : "AllData cannot be null";

        this.allData = allData;
        this.updateFilters();
    }

    private boolean classInvariant() {
        return allData != null &&
               filteredProfessors != null &&
               filteredGroups != null &&
               filteredDisciplines != null &&
               filteredRooms != null &&
               selectedProfessor != null &&
               selectedGroup != null &&
               selectedDiscipline != null &&
               selectedRoom != null;
    }

    public void setCalendarRef(Calendar calendarRef) {
        assert calendarRef != null : "Calendar reference cannot be null";

        this.calendarRef = calendarRef;
    }

    private void updateFilters() {
        assert classInvariant() : "Class invariant violated in Filters";

        // Preserve the current selections
        Professor previouslySelectedProfessor = selectedProfessor.get() > 0 ? filteredProfessors.get(selectedProfessor.get()) : null;
        Discipline previouslySelectedDiscipline = selectedDiscipline.get() > 0 ? filteredDisciplines.get(selectedDiscipline.get()) : null;
        Group previouslySelectedGroup = selectedGroup.get() > 0 ? filteredGroups.get(selectedGroup.get()) : null;
        Room previouslySelectedRoom = selectedRoom.get() > 0 ? filteredRooms.get(selectedRoom.get()) : null;

        // Filter professors based on the selected discipline
        filteredProfessors = new ArrayList<>();
        if (selectedDiscipline.get() > 0) {
            Discipline selectedDisciplineObj = previouslySelectedDiscipline;
            filteredProfessors.add(new Professor()); // Add a placeholder for "All"

            List<Professor> courseProfs = selectedDisciplineObj.getCourseProfs();
            List<Professor> labProfs = selectedDisciplineObj.getLaboratoryProfs();

            for (Professor professor : allData.getProfessors()) {
                if (courseProfs.contains(professor) || labProfs.contains(professor)) {
                    filteredProfessors.add(professor);
                }
            }

            // Restore the selected professor if possible
            if (previouslySelectedProfessor != null && filteredProfessors.contains(previouslySelectedProfessor)) {
                selectedProfessor.set(filteredProfessors.indexOf(previouslySelectedProfessor));
            } else {
                selectedProfessor.set(0); // Reset to "All" if not found
            }
        } else {
            filteredProfessors.add(new Professor()); // Add a placeholder for "All"
            filteredProfessors.addAll(allData.getProfessors());
        }

        // Filter disciplines based on the selected professor
        filteredDisciplines = new ArrayList<>();
        if (selectedProfessor.get() > 0 || selectedGroup.get() > 0) {
            Professor selectedProfessorObj = previouslySelectedProfessor;
            Group selectedGroupObj = previouslySelectedGroup;

            filteredDisciplines.add(new Discipline()); // Add a placeholder for "All"
            for (Discipline discipline : allData.getDisciplines()) {

                boolean hasCourseProf = discipline.getCourseProfs().contains(selectedProfessorObj);
                boolean hasLabProf = discipline.getLaboratoryProfs().contains(selectedProfessorObj);

                boolean hasProf = selectedProfessorObj == null || hasCourseProf || hasLabProf;
                boolean hasGroup = selectedGroupObj == null || selectedGroupObj.getDisciplines().contains(discipline);

                if (hasProf && hasGroup)
                    filteredDisciplines.add(discipline);
                }
        } else {
            filteredDisciplines.add(new Discipline()); // Add a placeholder for "All"
            filteredDisciplines.addAll(allData.getDisciplines());
        }

        // Restore the selected discipline if possible
        if (previouslySelectedDiscipline != null && filteredDisciplines.contains(previouslySelectedDiscipline)) {
            selectedDiscipline.set(filteredDisciplines.indexOf(previouslySelectedDiscipline));
        } else {
            selectedDiscipline.set(0); // Reset to "All" if not found
        }

        // Filter groups based on the filtered disciplines
        filteredGroups = new ArrayList<>();
        if (selectedDiscipline.get() > 0) {
            Discipline selectedDisciplineObj = previouslySelectedDiscipline;
            filteredGroups.add(new Group()); // Add a placeholder for "All"
            for (Group group : allData.getGroups()) {
                if (group.getDisciplines().contains(selectedDisciplineObj)) {
                    filteredGroups.add(group);
                }
            }
        } else {
            filteredGroups.add(new Group()); // Add a placeholder for "All"
            filteredGroups.addAll(allData.getGroups());
        }

        // Restore the selected group if possible
        if (previouslySelectedGroup != null && filteredGroups.contains(previouslySelectedGroup)) {
            selectedGroup.set(filteredGroups.indexOf(previouslySelectedGroup));
        } else {
            selectedGroup.set(0); // Reset to "All" if not found
        }

        // Filter rooms (if needed, based on other criteria)
        filteredRooms = new ArrayList<>();
        filteredRooms.add(new Room()); // Add a placeholder for "All"
        filteredRooms.addAll(allData.getRooms());

        // Restore the selected room if possible
        if (previouslySelectedRoom != null && filteredRooms.contains(previouslySelectedRoom)) {
            selectedRoom.set(filteredRooms.indexOf(previouslySelectedRoom));
        } else {
            selectedRoom.set(0); // Reset to "All" if not found
        }

        if(previouslySelectedProfessor == null &&
            previouslySelectedDiscipline == null &&
            previouslySelectedGroup == null &&
            previouslySelectedRoom == null) {

            _areFiltersValid = false;
        } else {
            _areFiltersValid = true;

            assert this.calendarRef != null;
            this.calendarRef.updateFilters();
        }

        assert classInvariant() : "Class invariant violated after updateFilters";
    }

    public List<Professor> getFilteredProfessors() {
        assert classInvariant() : "Class invariant violated in getFilteredProfessors";
        return filteredProfessors;
    }

    public List<Group> getFilteredGroups() {
        assert classInvariant() : "Class invariant violated in getFilteredGroups";
        return filteredGroups;
    }

    public List<Discipline> getFilteredDisciplines() {
        assert classInvariant() : "Class invariant violated in getFilteredDisciplines";
        return filteredDisciplines;
    }

    public List<Room> getFilteredRooms() {
        assert classInvariant() : "Class invariant violated in getFilteredRooms";
        return filteredRooms;
    }

    public boolean areFiltersValid() {
        assert classInvariant() : "Class invariant violated in areFiltersValid";
        return _areFiltersValid;
    }

    /**
     * Returns the currently selected Professor object.
     * @return The selected Professor or null if "All" is selected
     */
    public Professor getSelectedProfessor() {
        assert classInvariant() : "Class invariant violated in getSelectedProfessor";
        
        if (selectedProfessor.get() <= 0) {
            return null; // Return null if "All" is selected (index 0) or no professors available
        }
        return filteredProfessors.get(selectedProfessor.get());
    }

    /**
     * Returns the currently selected Group object.
     * @return The selected Group or null if "All" is selected
     */
    public Group getSelectedGroup() {
        assert classInvariant() : "Class invariant violated in getSelectedGroup";

        if (selectedGroup.get() <= 0) {
            return null; // Return null if "All" is selected (index 0) or no groups available
        }
        return filteredGroups.get(selectedGroup.get());
    }

    /**
     * Returns the currently selected Discipline object.
     * @return The selected Discipline or null if "All" is selected
     */
    public Discipline getSelectedDiscipline() {
        assert classInvariant() : "Class invariant violated in getSelectedDiscipline";
        
        if (selectedDiscipline.get() <= 0) {
            return null; // Return null if "All" is selected (index 0) or no disciplines available
        }
        return filteredDisciplines.get(selectedDiscipline.get());
    }

    /**
     * Returns the currently selected Room object.
     * @return The selected Room or null if "All" is selected
     */
    public Room getSelectedRoom() {
        assert classInvariant() : "Class invariant violated in getSelectedRoom";
        
        if (selectedRoom.get() <= 0) {
            return null; // Return null if "All" is selected (index 0) or no rooms available
        }
        return filteredRooms.get(selectedRoom.get());
    }

    public void process() {
        assert classInvariant() : "Class invariant violated in process";

        ImGui.begin("Filters");
            float window_width = ImGui.getWindowWidth();
            float one_filter_width = window_width * 0.7f * 0.248f;

            // ----- Professors filter
            List<Professor> professors = getFilteredProfessors();
            ImGui.setNextItemWidth(one_filter_width);

            Professor selected_professor = professors.get(selectedProfessor.get());

            if(ImGui.beginCombo("##Professor", "Professor: " + selected_professor.getName())) {
                ImGui.setNextItemWidth(one_filter_width - 10);

                if(ImGui.inputText("##ProfessorText", professorFilterInputText)) {
                    professorFilter.setInputBuffer(professorFilterInputText.toString());
                    professorFilter.build();
                }

                for (int i = 0; i < professors.size(); i++) {
                    boolean isSelected = (selectedProfessor.get() == i);

                    if (professorFilter.passFilter(professors.get(i).getName()) && ImGui.selectable(professors.get(i).getName(), isSelected)) {
                        selectedProfessor.set(i); // Update the selected index
                        updateFilters();
                    }

                    if (isSelected) {
                        ImGui.setItemDefaultFocus();
                    }
                }
                ImGui.endCombo();
            }

            ImGui.sameLine();

            // ----- Group filter
            List<Group> groups = getFilteredGroups();
            ImGui.setNextItemWidth(one_filter_width);
            if(ImGui.beginCombo("##Group", "Group: " + groups.get(selectedGroup.get()).getName())) {
                ImGui.setNextItemWidth(one_filter_width - 10);

                if(ImGui.inputText("##GroupText", groupFilterInputText)) {
                    groupFilter.setInputBuffer(groupFilterInputText.toString());
                    groupFilter.build();
                }

                for (int i = 0; i < groups.size(); i++) {
                    boolean isSelected = (selectedGroup.get() == i);

                    if (groupFilter.passFilter(groups.get(i).getName()) && ImGui.selectable(groups.get(i).getName(), isSelected)) {
                        selectedGroup.set(i); // Update the selected index
                        updateFilters();
                    }

                    if (isSelected) {
                        ImGui.setItemDefaultFocus();
                    }
                }
                ImGui.endCombo();
            }

            ImGui.sameLine();

            // --- Discipline filter
            List<Discipline> disciplines = getFilteredDisciplines();
            ImGui.setNextItemWidth(one_filter_width);
            if(ImGui.beginCombo("##Discipline", "Discipline: " + disciplines.get(selectedDiscipline.get()).getName())) {
                ImGui.setNextItemWidth(one_filter_width - 10);

                if(ImGui.inputText("##DisciplineText", disciplineFilterInputText)) {
                    disciplineFilter.setInputBuffer(disciplineFilterInputText.toString());
                    disciplineFilter.build();
                }

                for (int i = 0; i < disciplines.size(); i++) {
                    boolean isSelected = (selectedDiscipline.get() == i);

                    if (disciplineFilter.passFilter(disciplines.get(i).getName()) && ImGui.selectable(disciplines.get(i).getName(), isSelected)) {
                        selectedDiscipline.set(i); // Update the selected index
                        updateFilters();
                    }

                    if (isSelected) {
                        ImGui.setItemDefaultFocus();
                    }
                }
                ImGui.endCombo();
            }
            ImGui.sameLine();
            
            // --- Room filter
            List<Room> rooms = getFilteredRooms();
            ImGui.setNextItemWidth(one_filter_width);

            if(ImGui.beginCombo("##Room", "Room: " + rooms.get(selectedRoom.get()).getName())) {
                ImGui.setNextItemWidth(one_filter_width - 10);

                if(ImGui.inputText("##RoomText", roomFilterInputText)) {
                    roomFilter.setInputBuffer(roomFilterInputText.toString());
                    roomFilter.build();
                }

                for (int i = 0; i < rooms.size(); i++) {
                    boolean isSelected = (selectedRoom.get() == i);

                    if (roomFilter.passFilter(rooms.get(i).getName()) && ImGui.selectable(rooms.get(i).getName(), isSelected)) {
                        selectedRoom.set(i); // Update the selected index
                        updateFilters();
                    }

                    if (isSelected) {
                        ImGui.setItemDefaultFocus();
                    }
                }
                ImGui.endCombo();
            }
            ImGui.sameLine();


        ImGui.end();
        assert classInvariant() : "Class invariant violated at the end of process";
    }
}

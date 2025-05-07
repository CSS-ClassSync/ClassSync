package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import imgui.ImGui;
import imgui.ImGuiTextFilter;
import imgui.type.ImInt;
import imgui.type.ImString;
import io.github.ClassSyncCSS.ClassSync.Domain.*;

import java.util.ArrayList;
import java.util.List;

public class Filters {
    private ImInt selectedProfessor = new ImInt();
    private ImString professorFilterInputText = new ImString();
    private ImGuiTextFilter professorFilter = new ImGuiTextFilter();

    private ImInt selectedGroup = new ImInt();
    private ImString groupFilterInputText = new ImString();
    private ImGuiTextFilter groupFilter = new ImGuiTextFilter();

    private ImInt selectedDiscipline = new ImInt();
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

    public Filters(AllData allData) {
        this.allData = allData;
        this.updateFilters();
    }

    private void updateFilters() {
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
        if (selectedProfessor.get() > 0) {
            Professor selectedProfessorObj = previouslySelectedProfessor;
            filteredDisciplines.add(new Discipline()); // Add a placeholder for "All"
            for (Discipline discipline : allData.getDisciplines()) {
                if (discipline.getCourseProfs().contains(selectedProfessorObj) || discipline.getLaboratoryProfs().contains(selectedProfessorObj)) {
                    filteredDisciplines.add(discipline);
                }
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
        }
    }

    public List<Professor> getFilteredProfessors() {
        return filteredProfessors;
    }

    public List<Group> getFilteredGroups() {
        return filteredGroups;
    }

    public List<Discipline> getFilteredDisciplines() {
        return filteredDisciplines;
    }

    public List<Room> getFilteredRooms() {
        return filteredRooms;
    }

    public boolean areFiltersValid() {
        return _areFiltersValid;
    }

    public void process() {
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
    }
}

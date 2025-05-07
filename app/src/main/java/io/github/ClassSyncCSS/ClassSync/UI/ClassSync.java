package io.github.ClassSyncCSS.ClassSync.UI;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.*;
import imgui.type.ImString;
import io.github.ClassSyncCSS.ClassSync.UI.Controls.Calendar;
import io.github.ClassSyncCSS.ClassSync.UI.Controls.Filters;
import io.github.ClassSyncCSS.ClassSync.UI.Controls.SidePane;

import java.util.ArrayList;

public class ClassSync extends Application {
    ImString input = new ImString();
    Filters filters;
    Calendar calendar = new Calendar();
    SidePane sidePane = new SidePane();

    public ClassSync() {
        var professors = new ArrayList<String>();
        professors.add("One");
        professors.add("Two");
        professors.add("Three");
        this.filters = new Filters(professors);
    }


    @Override
    protected void configure(Configuration config) {
        config.setTitle("Dear ImGui is Awesome!");
    }

    @Override
    public void preRun() {
        var io = ImGui.getIO();
        io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
    }

    @Override
    public void process() {
        ImGui.dockSpaceOverViewport();

        this.filters.process();
        this.calendar.process();
        this.sidePane.process();
    }

    public static void main(String[] args) {
        launch(new ClassSync());
    }
}
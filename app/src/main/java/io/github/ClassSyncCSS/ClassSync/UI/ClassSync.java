package io.github.ClassSyncCSS.ClassSync.UI;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.*;
import io.github.ClassSyncCSS.ClassSync.Domain.AllData;
import io.github.ClassSyncCSS.ClassSync.UI.Controls.*;

import java.util.ArrayList;

public class ClassSync extends Application {
    Filters filters;
    Calendar calendar = new Calendar();
    SidePane sidePane = new SidePane();
    Debug debug = new Debug();
    AllData allData;
    Exporter exporter = new Exporter();

    public ClassSync() {
        this.allData = AllData.load();
//        System.out.println(allData);

        this.filters = new Filters(allData);
        this.calendar.setSidePaneRef(this.sidePane);
        this.calendar.setFiltersRef(this.filters);
        this.calendar.setData(allData);
        this.exporter.setCalendarRef(calendar);

        this.sidePane.setAllData(allData);
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

        ImGui.getIO().setFramerate(144.0f);

        this.filters.process();
        this.calendar.process();
        this.sidePane.process();
        this.debug.process();
        this.exporter.process();
    }

    public static void main(String[] args) {
        launch(new ClassSync());
    }
}

package io.github.ClassSyncCSS.ClassSync.UI.Controls;

import imgui.ImGui;
import io.github.ClassSyncCSS.ClassSync.Domain.AllData;
import io.github.ClassSyncCSS.ClassSync.Domain.ScheduleExporter;

public class Exporter {


    Calendar calendar;
    AllData data;

    public void setCalendarRef(Calendar cal) {
        this.calendar = cal;
    }

    public void setAllData(AllData dat) {
        this.data = dat;
    }


    public void process() {
        ImGui.begin("Exporter");

            if(ImGui.button("Export")) {
                ScheduleExporter.exportScheduleToJson(calendar.fullTimeTable.getScheduleByGroupRaw(),
                        "test.json");
            }

        ImGui.end();
    }

}

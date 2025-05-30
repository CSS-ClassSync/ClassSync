package io.github.ClassSyncCSS.ClassSync.Domain;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AllData {
    private static final String COMMA_DELIMITER = ",";

    public void setProfessors(List<Professor> professors) {
        this.professors = professors;
    }

    public void setDisciplines(List<Discipline> disciplines) {
        this.disciplines = disciplines;
    }

    public void setSpecializations(List<Specialization> specializations) {
        this.specializations = specializations;
    }

    public void setYears(List<Year> years) {
        this.years = years;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    private List<Professor> professors;
    private List<Discipline> disciplines;
    private List<Specialization> specializations;
    private List<Year> years;
    private List<Group> groups;
    private List<Room> rooms;

    public AllData(List<Professor> professors, List<Discipline> disciplines, List<Specialization> specializations, List<Year> years, List<Group> groups, List<Room> rooms) {
        this.professors = professors;
        this.disciplines = disciplines;
        this.specializations = specializations;
        this.years = years;
        this.groups = groups;
        this.rooms = rooms;
    }

    public List<Professor> getProfessors() {
        return professors;
    }

    public List<Discipline> getDisciplines() {
        return disciplines;
    }

    public List<Specialization> getSpecializations() {
        return specializations;
    }

    public List<Year> getYears() {
        return years;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public static AllData load() {
        return load("src/main/resources");
    }

    public static AllData load(String path) {
        List<List<String>> yearsD = loadCsv(path.concat("/ani.csv"));
        List<List<String>> groupsD = loadCsv(path.concat("/grupe.csv"));
        List<List<String>> profsD = loadCsv(path.concat("/profesori.csv"));
        List<List<String>> disciplinesD = loadCsv(path.concat("/materii.csv"));
        List<List<String>> roomsD = loadCsv(path.concat("/sali.csv"));

        List<List<String>> disciplines_years = loadCsv(path.concat("/materii_an.csv"));
        List<List<String>> disciplines_profs = loadCsv(path.concat("/profesori_materii.csv"));

        // Assert that all CSVs are not empty (except rooms, which may be empty)
        assert yearsD != null && !yearsD.isEmpty() : "yearsD is empty";
        assert groupsD != null && !groupsD.isEmpty() : "groupsD is empty";
        assert profsD != null && !profsD.isEmpty() : "profsD is empty";
        assert disciplinesD != null && !disciplinesD.isEmpty() : "disciplinesD is empty";
        assert disciplines_years != null : "disciplines_years is null";
        assert disciplines_profs != null : "disciplines_profs is null";
        assert roomsD != null : "roomsD is null";

        // Assert that headers are correct
        assert yearsD.getFirst().size() >= 3 : "yearsD header has less than 3 columns";
        assert groupsD.getFirst().size() >= 3 : "groupsD header has less than 3 columns";
        assert profsD.getFirst().size() >= 2 : "profsD header has less than 2 columns";
        assert disciplinesD.getFirst().size() >= 3 : "disciplinesD header has less than 3 columns";
        assert roomsD.isEmpty() || roomsD.getFirst().size() >= 2 : "roomsD header has less than 2 columns";

        // Assert that all referenced years and specializations exist for groups
        for (List<String> gD : groupsD) {
            String yearSpecName = gD.getLast();
            boolean yearExists = yearsD.stream().anyMatch(y -> y.getFirst().equals(yearSpecName));
            assert yearExists : "Group references missing year: " + yearSpecName;
        }

        // Assert that all referenced disciplines and years exist for materii_an
        for (List<String> dy : disciplines_years) {
            String discId = dy.getFirst();
            String yearId = dy.getLast();
            boolean discExists = disciplinesD.stream().anyMatch(d -> d.getFirst().equals(discId));
            boolean yearExists = yearsD.stream().anyMatch(y -> y.getFirst().equals(yearId));
            assert discExists : "materii_an references missing discipline: " + discId;
            assert yearExists : "materii_an references missing year: " + yearId;
        }

        // Assert that all referenced professors and disciplines exist for profesori_materii
        for (List<String> pd : disciplines_profs) {
            String profId = pd.getFirst();
            String discId = pd.getLast();
            boolean profExists = profsD.stream().anyMatch(p -> p.getFirst().equals(profId));
            boolean discExists = disciplinesD.stream().anyMatch(d -> d.getFirst().equals(discId));
            assert profExists : "profesori_materii references missing professor: " + profId;
            assert discExists : "profesori_materii references missing discipline: " + discId;
        }

        List<String> discNames = disciplinesD.stream()
                .map(d -> Arrays.asList("Curs", "Laborator", "Seminar").contains(d.getLast()) ? d.get(1) : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()).stream().toList();

        List<String> yearNames = yearsD.stream()
                .map(y -> y.get(1))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()).stream().toList();

        List<String> specNames = yearsD.stream()
                .map(y -> !Objects.equals(y.get(2), "") ? y.get(2) : "Informatică")
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()).stream().toList();

        List<String> profNames = profsD.stream()
                .map(p -> p.get(1))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()).stream().toList();

        List<String> groupNames = groupsD.stream()
                .map(List::getFirst)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()).stream().toList();

        List<String> roomNames = roomsD.stream()
                .map(r -> Arrays.asList("Curs", "Laborator", "Seminar").contains(r.getLast().split("-")[0].trim()) ? r.getFirst() : null)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()).stream().toList();

        List<Professor> professors = new ArrayList<>();
        List<Discipline> disciplines = new ArrayList<>();
        List<Specialization> specializations = new ArrayList<>();
        List<Year> years = new ArrayList<>();
        List<Group> groups = new ArrayList<>();
        List<Room> rooms = new ArrayList<>();

        profNames.forEach(p -> {
            professors.add(new Professor(p, null, null));
        });

        discNames.forEach(d -> {
            disciplines.add(new Discipline(d, null, null));
        });

        specNames.forEach(s -> {
            specializations.add(new Specialization(s, null, null));
        });

        groupNames.forEach(g -> {
            groups.add(new Group(g, null, null));
        });

        yearNames.forEach(y -> {
            years.add(new Year(y, null));
        });

        roomNames.forEach(r -> {
            rooms.add(new Room(r, null));
        });

        for (List<String> gD : groupsD) {
            String groupName = gD.getFirst();
            Group g = groups.stream().filter(g3 -> g3.getName().equals(groupName)).findFirst().orElse(null);

//            System.out.println(g);

            List<String> groupTemp = groupsD.stream().filter(g2 -> g2.getFirst().equals(groupName)).findFirst().orElse(null);
            String yearSpecName = groupTemp.getLast();
            List<String> yearTemp = yearsD.stream()
                    .filter(y -> y.getFirst().equals(yearSpecName))
                    .findFirst().orElse(null);

            Year year = years.stream()
                    .filter(y -> y.getYear().equals(yearTemp.get(1)))
                    .findFirst().orElse(null);

//            System.out.println(year);

            Specialization specialization = specializations.stream()
                    .filter(s -> s.getName().equals(yearTemp.get(2)))
                    .findFirst().orElse(null);

//            System.out.println(specialization);

            List<Group> gs = specializations.get(specializations.indexOf(specialization)).getGroups();
            gs.add(g);

            specializations.get(specializations.indexOf(specialization)).setGroups(gs);

            if (!year.getSpecializations().contains(specialization))
            {
                List<Specialization> specs = years.get(years.indexOf(year)).getSpecializations();
                specs.add(specialization);
                years.get(years.indexOf(year)).setSpecializations(specs);
            }

            groups.get(groups.indexOf(g)).setYear(year);

//            System.out.println(g);
//            System.out.println(year);
//            System.out.println(specialization);
//            System.out.println("\n");
        };

        for (List<String> dD : disciplinesD) {
            Discipline d = disciplines.stream().filter(d2 -> d2.getName().equals(dD.get(1))).findFirst().orElse(null);

            if (d == null)
            {
                continue;
            }

            List<List<String>> discYears = disciplines_years.stream()
                    .filter(dy -> dy.getFirst().equals(dD.get(0)))
                    .toList();

            for (List<String> discYear : discYears)
            {
                List<String> yearTemp = yearsD.stream()
                        .filter(y -> y.getFirst().equals(discYear.getLast()))
                        .findFirst().orElse(null);

                Year y = years.stream()
                        .filter(y2 -> y2.getYear().equals(yearTemp.get(1)))
                        .findFirst().orElse(null);

                Specialization s = specializations.stream()
                        .filter(s2 -> s2.getName().equals(yearTemp.get(2)))
                        .findFirst().orElse(null);

                List<Group> gs = groups.stream()
                        .filter(g2 -> g2.getYear().equals(y) && s.getGroups().contains(g2))
                        .toList();

                if (!s.getDisciplines().contains(d))
                {
                    List<Discipline> ds = specializations.get(specializations.indexOf(s)).getDisciplines();
                    ds.add(d);
                    specializations.get(specializations.indexOf(s)).setDisciplines(ds);
                }

                for (Group g : gs)
                {
                    if (!g.getDisciplines().contains(d))
                    {
                        List<Discipline> ds = groups.get(groups.indexOf(g)).getDisciplines();
                        ds.add(d);
                        groups.get(groups.indexOf(g)).setDisciplines(ds);
                    }
                }
            }

        };

        for (List<String> pD : profsD) {
            Professor p = professors.stream().filter(p2 -> p2.getName().equals(pD.get(1))).findFirst().orElse(null);

            List<List<String>> profDisc = disciplines_profs.stream()
                    .filter(pd -> pd.getFirst().equals(pD.get(0)))
                    .toList();

            for (List<String> profDiscipline : profDisc)
            {
                List<String> discTemp = disciplinesD.stream()
                        .filter(d -> d.getFirst().equals(profDiscipline.getLast()))
                        .findFirst().orElse(null);

                if (discTemp == null)
                {
                    continue;
                }

                Discipline d = disciplines.stream()
                        .filter(d2 -> d2.getName().equals(discTemp.get(1)))
                        .findFirst().orElse(null);

                if (d == null)
                {
                    continue;
                }

                ActivityType activityType = null;

                switch (discTemp.getLast())
                {
                    case "Curs" -> activityType = ActivityType.Course;
                    case "Laborator" -> activityType = ActivityType.Lab;
                    case "Seminar" -> activityType = ActivityType.Seminary;
                }

                Map<Discipline, List<ActivityType>> ds = professors.get(professors.indexOf(p)).getDisciplines();
                if (ds.containsKey(d))
                {
                    List<ActivityType> at = ds.get(d);
                    if (!at.contains(activityType))
                    {
                        at.add(activityType);
                    }
                }
                else
                {
                    List<ActivityType> at = new ArrayList<>();
                    at.add(activityType);
                    ds.put(d, at);
                }

                List<ActivityType> allTypes = new ArrayList<>();

                for (List<ActivityType> lat : ds.values())
                {
                    for (ActivityType at : lat)
                    {
                        if (!allTypes.contains(at))
                        {
                            allTypes.add(at);
                        }
                    }
                }

                allTypes = new HashSet<>(allTypes).stream().toList();

                ProfType profType = null;

                if (allTypes.size() > 1 && allTypes.contains(ActivityType.Course))
                {
                    profType = ProfType.All;
                }
                else if (allTypes.size() == 1 && (allTypes.contains(ActivityType.Lab) || allTypes.contains(ActivityType.Seminary)))
                {
                    profType = ProfType.Laboratory;
                }
                else if (allTypes.size() == 1 && allTypes.contains(ActivityType.Course))
                {
                    profType = ProfType.Course;
                }

                professors.get(professors.indexOf(p)).setDisciplines(ds);
                professors.get(professors.indexOf(p)).setType(profType);

                if (activityType == ActivityType.Course) {
                    List<Professor> ds2 = d.getCourseProfs();
                    if (!ds2.contains(p)) {
                        ds2.add(p);
                    }
                    disciplines.get(disciplines.indexOf(d)).setCourseProfs(ds2);
                }
                else
                {
                    List<Professor> ds2 = d.getLaboratoryProfs();
                    if (!ds2.contains(p)) {
                        ds2.add(p);
                    }
                    disciplines.get(disciplines.indexOf(d)).setLaboratoryProfs(ds2);
                }
            }
        };

        for (List<String> rD : roomsD) {
            Room r = rooms.stream().filter(r2 -> r2.getName().equals(rD.getFirst())).findFirst().orElse(null);

            if (r == null)
            {
                continue;
            }

            ActivityType activityType = null;

            switch (rD.getLast())
            {
                case "Curs" -> activityType = ActivityType.Course;
                case "Laborator" -> activityType = ActivityType.Lab;
                case "Seminar" -> activityType = ActivityType.Seminary;
            }

            List<ActivityType> atl = rooms.get(rooms.indexOf(r)).getType();
            if (!atl.contains(activityType))
            {
                atl.add(activityType);
            }
            rooms.get(rooms.indexOf(r)).setType(atl);
        };

        // --- Post-condition asserts ---

        // All lists must not be null
        assert professors != null : "Postcondition failed: professors list is null";
        assert disciplines != null : "Postcondition failed: disciplines list is null";
        assert specializations != null : "Postcondition failed: specializations list is null";
        assert years != null : "Postcondition failed: years list is null";
        assert groups != null : "Postcondition failed: groups list is null";
        assert rooms != null : "Postcondition failed: rooms list is null";

        // All professors, disciplines, specializations, years, groups, rooms must have non-null names
        professors.forEach(p -> {
            assert p.getName() != null && !p.getName().isEmpty() : "Postcondition failed: professor with null/empty name";
        });
        disciplines.forEach(d -> {
            assert d.getName() != null && !d.getName().isEmpty() : "Postcondition failed: discipline with null/empty name";
        });
        specializations.forEach(s -> {
            assert s.getName() != null && !s.getName().isEmpty() : "Postcondition failed: specialization with null/empty name";
        });
        years.forEach(y -> {
            assert y.getYear() != null && !y.getYear().isEmpty() : "Postcondition failed: year with null/empty value";
        });
        groups.forEach(g -> {
            assert g.getName() != null && !g.getName().isEmpty() : "Postcondition failed: group with null/empty name";
        });
        rooms.forEach(r -> {
            assert r.getName() != null && !r.getName().isEmpty() : "Postcondition failed: room with null/empty name";
        });

        // All groups must have a year assigned
        groups.forEach(g -> {
            assert g.getYear() != null : "Postcondition failed: group without year";
        });

        // All years must have specializations list (possibly empty, but not null)
        years.forEach(y -> {
            assert y.getSpecializations() != null : "Postcondition failed: year with null specializations";
        });

        // All specializations must have groups and disciplines lists (possibly empty, but not null)
        specializations.forEach(s -> {
            assert s.getGroups() != null : "Postcondition failed: specialization with null groups";
            assert s.getDisciplines() != null : "Postcondition failed: specialization with null disciplines";
        });

        // All disciplines must have courseProfs and laboratoryProfs lists (possibly empty, but not null)
        disciplines.forEach(d -> {
            assert d.getCourseProfs() != null : "Postcondition failed: discipline with null courseProfs";
            assert d.getLaboratoryProfs() != null : "Postcondition failed: discipline with null laboratoryProfs";
        });

        // All professors must have disciplines map (possibly empty, but not null)
        professors.forEach(p -> {
            assert p.getDisciplines() != null : "Postcondition failed: professor with null disciplines map";
        });

        // All rooms must have type list (possibly empty, but not null)
        rooms.forEach(r -> {
            assert r.getType() != null : "Postcondition failed: room with null type list";
        });

        return new AllData(professors, disciplines, specializations, years, groups, rooms);
    }

    public static List<List<String>> loadCsv(String filePath) {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.contains(COMMA_DELIMITER))
                {
                    throw new RuntimeException("Invalid CSV format: " + filePath);
                }
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
            if (records.stream().map(List::size).distinct().count() != 1)
            {
                throw new IndexOutOfBoundsException("Invalid CSV format: " + filePath);
            }
            if (records.getFirst().contains("id"))
            {
                int idIndex = records.getFirst().indexOf("id");
                Set ids = records.stream().filter(r -> records.indexOf(r) != 0).map(r -> r.get(idIndex)).collect(Collectors.toSet());
                if (ids.size() != records.size() - 1)
                {
                    throw new RuntimeException("Duplicate IDs in CSV: " + filePath);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        records.removeFirst();
        return records;
    }

    @Override
    public String toString() {
        return "AllData{" +
                "professors=" + professors +
                ", disciplines=" + disciplines +
                ", specializations=" + specializations +
                ", years=" + years +
                ", groups=" + groups +
                ", rooms=" + rooms +
                '}';
    }
}

package com.digdes.school;

import java.util.*;

public class JavaSchoolStarter {
    private final List<Map<String, Object>> users = new ArrayList<>();
    private final Map<String, Object> tempUser = new HashMap<>();

    public JavaSchoolStarter() {
    }

    public List<Map<String, Object>> execute(String request) throws Exception {
        String formattedRequest = request.trim();
        String method = formattedRequest.substring(0, 6).toLowerCase();

        switch (method) {
            case "insert" -> {
                addUser(formattedRequest);
                return users;
            }
            case "update" -> {
                updateUsers(formattedRequest);
                return users;
            }
            case "delete" -> {
                deleteUsers(formattedRequest);
                return users;
            }
            case "select" -> {
                return selectUsers(formattedRequest);
            }
            default -> System.out.println("Unknown");
        }

        return new ArrayList<>();
    }

    private void updateUsers(String request) {
        String values ="", where="";
        if (request.contains("where")) {
            String valuesAndWhere = request.split("(?i)values")[1];
             values = valuesAndWhere.split("(?i)where")[0];
             where = "where " + valuesAndWhere.split("(?i)where")[1];

        } else {
             values = request.split("(?i)values")[1];
            setValues(values);
        }

        setValues(values);

        List<Map<String, Object>> updatedUsers = selectUsers("select " + where);
        updatedUsers.forEach(user -> {
            tempUser.keySet().forEach(key -> {
                user.put(key, tempUser.get(key));
            });
        });

    }

    private void deleteUsers(String request) {
        if (request.equalsIgnoreCase("delete")) {
            users.clear();
            return;
        }

        List<Map<String, Object>> deletedUsers = selectUsers(request);
        users.removeIf(deletedUsers::contains);
    }

    private List<Map<String, Object>> selectUsers(String request) {
        List<Map<String, Object>> selectedUsers = new ArrayList<>();
        if (request.trim().equalsIgnoreCase("select")) {
            return users;
        }

        try {
            String template = request.split("(?i)where")[1];
            for (Map<String, Object> user : users) {
                String whereClause = template.replaceAll("(?i)'id'", user.get("id") + "");
                whereClause = whereClause.replaceAll("(?i)'age'", user.get("age") + "");
                whereClause = whereClause.replaceAll("(?i)lastname", user.get("lastname") + "");
                whereClause = whereClause.replaceAll("(?i)'cost'", user.get("cost") + "");
                whereClause = whereClause.replaceAll("(?i)'active'", user.get("active") + "");
                if (EvaluateExpression.evaluateExpression(whereClause)) {
                    selectedUsers.add(user);
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException("Wrong 'where' clause");
        }
        return selectedUsers;
    }

    private void addUser(String request) {
        String values = request.split("(?i)values")[1];
        setValues(values);

        if (tempUser.get("id") == null
                && tempUser.get("lastname") == null && tempUser.get("age") == null
                && tempUser.get("cost") == null && tempUser.get("active") == null) {
            throw new RuntimeException("All fields are equal to zero. The record can't be created");
        }

        Map<String, Object> newUser = new HashMap<>();
        newUser.put("id", tempUser.get("id"));
        newUser.put("lastname", tempUser.get("lastname"));
        newUser.put("age", tempUser.get("age"));
        newUser.put("cost", tempUser.get("cost"));
        newUser.put("active", tempUser.get("active"));

        tempUser.clear();
        users.add(newUser);
    }

    private void setValues(String values) {
        List<String> keyValues = List.of(values.split(","));
        keyValues.forEach(keyValue -> {
            String key = keyValue.split("=")[0];
            key = key.substring(key.indexOf('\'') + 1, key.lastIndexOf('\'')).trim().toLowerCase();
            String value = keyValue.split("=")[1].trim();

            switch (key) {
                case "lastname" -> {
                    if (!validateLastName(value)) {
                        throw new RuntimeException("Wrong value for field lastname");
                    }
                    String lastname = (value.equals("null")) ? null : value.substring(value.indexOf('\'') + 1, value.lastIndexOf('\''));
                    tempUser.put("lastname", lastname);
                }
                case "id" -> {
                    if (!validateId(value)) {
                        throw new RuntimeException("Wrong value for field id");
                    }
                    Long id = (value.equalsIgnoreCase("null")) ? null : Long.parseLong(value);
                    tempUser.put("id", id);
                }
                case "age" -> {
                    if (!validateAge(value)) {
                        throw new RuntimeException("Wrong value for field age");
                    }
                    Long age = (value.equalsIgnoreCase("null")) ? null : Long.parseLong(value);
                    tempUser.put("age", age);
                }
                case "cost" -> {
                    if (!validateCost(value)) {
                        throw new RuntimeException("Wrong value for field cost");
                    }
                    Double cost = (value.equalsIgnoreCase("null")) ? null : Double.parseDouble(value);
                    tempUser.put("cost", cost);
                }
                case "active" -> {
                    if (!validateActive(value)) {
                        throw new RuntimeException("Wrong value for field active");
                    }
                    Boolean active = (value.equalsIgnoreCase("null")) ? null : Boolean.parseBoolean(value);
                    tempUser.put("active", active);
                }
                default -> {
                    throw new RuntimeException("Unknown field" + key);
                }
            }
        });
    }

    private boolean validateLastName(String lastname) {
        if (lastname.equalsIgnoreCase("null")) {
            return true;
        }
        return lastname.startsWith("'") && lastname.endsWith("'");
    }

    private boolean validateId(String id) {
        if (id.equalsIgnoreCase("null")) return true;
        return isNumeric(id);
    }

    private boolean validateAge(String age) {
        if (age.equalsIgnoreCase("null")) return true;
        return isNumeric(age);
    }

    private boolean validateCost(String cost) {
        if (cost.equalsIgnoreCase("null")) return true;
        return isNumeric(cost);
    }

    private boolean validateActive(String active) {
        return active.equalsIgnoreCase("true")
                || active.equalsIgnoreCase("false")
                || active.equalsIgnoreCase("null");
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

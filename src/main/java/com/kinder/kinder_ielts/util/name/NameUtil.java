package com.kinder.kinder_ielts.util.name;

import com.kinder.kinder_ielts.constant.Role;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.entity.Tutor;

public class NameUtil {
    public static NameParts splitName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return null;
        }

        String[] parts = fullName.trim().split("\\s+"); // Split by one or more spaces

        String lastName = parts[0]; // First part is the first name
        String firstName = parts.length > 1 ? parts[parts.length - 1] : null; // Last part is the last name
        String middleName = parts.length > 2
                ? String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length - 1))
                : ""; // Combine middle parts

        if (lastName.isEmpty()) { lastName = null;}
        if (middleName.isEmpty()) { middleName = null;}
        return new NameParts(firstName, middleName, lastName);
    }

    public static String getFullName(Tutor tutor){
        StringBuilder fullName = new StringBuilder();
        fullName.append(tutor.getLastName());
        if (tutor.getMiddleName() != null)
            fullName.append(" ").append(tutor.getMiddleName());
        if (tutor.getFirstName() != null)
            fullName.append(" ").append(tutor.getFirstName());
        return fullName.toString();
    }

    public static String getFullName(Student student){
        StringBuilder fullName = new StringBuilder();
        fullName.append(student.getLastName());
        if (student.getMiddleName() != null)
            fullName.append(" ").append(student.getMiddleName());
        if (student.getFirstName() != null)
            fullName.append(" ").append(student.getFirstName());
        return fullName.toString();
    }
}

package com.kinder.kinder_ielts.service;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.classroom.CreateClassroomRequest;
import com.kinder.kinder_ielts.dto.request.classroom.UpdateClassroomRequest;
import com.kinder.kinder_ielts.dto.request.classroom.UpdateClassroomTutorRequest;
import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.exception.NotFoundException;
import com.kinder.kinder_ielts.exception.SqlException;

/**
 * Service interface for managing Classroom entities.
 * <p>
 * This service provides methods for creating, retrieving, updating, and deleting classrooms,
 * as well as managing classroom-specific relationships like tutors and courses.
 * </p>
 *
 * @author
 * @version 1.0
 */
public interface ClassroomService {

    /**
     * Create a new classroom with the provided details.
     *
     * <p>This method will:
     * <ul>
     *   <li>Map the incoming request to a Classroom entity.</li>
     *   <li>Assign a creator account to the classroom.</li>
     *   <li>Validate and associate tutors with the classroom (if provided).</li>
     *   <li>Assign the classroom to a course.</li>
     * </ul>
     * </p>
     *
     * @param request The request object containing classroom creation details.
     * @return {@link ClassroomResponse} The response containing classroom details.
     * @throws NotFoundException if the account or course is not found.
     * @throws SqlException if an error occurs during database operations.
     *
     * @example
     * <pre>
     * {@code
     * CreateClassRequest request = new CreateClassRequest();
     * request.setCourseId("course123");
     * request.setTutorIds(List.of("tutor1", "tutor2"));
     * ClassroomResponse response = classroomService.createClassroom(request);
     * System.out.println(response);
     * }
     * </pre>
     */
    ClassroomResponse createClassroom(CreateClassroomRequest request, String message);

    /**
     * Fetch basic information of a classroom by its ID.
     *
     * @param id The unique identifier of the classroom.
     * @return {@link ClassroomResponse} Containing basic classroom details.
     * @throws NotFoundException if the classroom is not found.
     *
     * @example
     * <pre>
     * {@code
     * String classroomId = "class123";
     * ClassroomResponse response = classroomService.getInfo(classroomId);
     * System.out.println(response);
     * }
     * </pre>
     */
    ClassroomResponse getInfo(String id);

    /**
     * Fetch detailed information of a classroom by its ID.
     *
     * @param id The unique identifier of the classroom.
     * @return {@link ClassroomResponse} Containing detailed classroom details.
     * @throws NotFoundException if the classroom is not found.
     *
     * @example
     * <pre>
     * {@code
     * String classroomId = "class123";
     * ClassroomResponse response = classroomService.getDetail(classroomId);
     * System.out.println(response);
     * }
     * </pre>
     */
    ClassroomResponse getDetail(String id);

    /**
     * Delete a classroom by its ID.
     *
     * <p>This method performs a logical deletion (soft delete) of the classroom.</p>
     *
     * @param id The unique identifier of the classroom.
     * @return {@link ResponseData} Containing status and message of the deletion operation.
     * @throws NotFoundException if the classroom is not found.
     *
     * @example
     * <pre>
     * {@code
     * String classroomId = "class123";
     * ResponseData response = classroomService.deleteCourse(classroomId);
     * System.out.println(response.getMessage());
     * }
     * </pre>
     */
    ResponseData deleteCourse(String id);

    /**
     * Update information of an existing classroom.
     *
     * <p>Only the fields provided in the {@link UpdateClassroomRequest} will be updated.</p>
     *
     * @param id      The unique identifier of the classroom.
     * @param request The request object containing updated classroom details.
     * @return {@link ClassroomResponse} The response containing updated classroom details.
     * @throws NotFoundException if the classroom is not found.
     * @throws SqlException if an error occurs during database operations.
     *
     * @example
     * <pre>
     * {@code
     * String classroomId = "class123";
     * UpdateClassroomRequest request = new UpdateClassroomRequest();
     * request.setDescription("Updated Description");
     * ClassroomResponse response = classroomService.updateInfo(classroomId, request);
     * System.out.println(response);
     * }
     * </pre>
     */
    ClassroomResponse updateInfo(String id, UpdateClassroomRequest request);

    /**
     * Update tutors assigned to a classroom.
     *
     * <p>This method will:
     * <ul>
     *   <li>Validate if the provided tutors exist in the associated course.</li>
     *   <li>Update the list of assigned tutors for the classroom.</li>
     * </ul>
     * </p>
     *
     * @param id      The unique identifier of the classroom.
     * @param request The request object containing the list of tutor IDs to be updated.
     * @return {@link ClassroomResponse} The response containing updated classroom tutor details.
     * @throws NotFoundException if the classroom or tutors are not found.
     * @throws SqlException if an error occurs during database operations.
     *
     * @example
     * <pre>
     * {@code
     * String classroomId = "class123";
     * UpdateClassroomTutorRequest request = new UpdateClassroomTutorRequest();
     * request.setTutorIds(List.of("tutor1", "tutor2"));
     * ClassroomResponse response = classroomService.updateTutors(classroomId, request);
     * System.out.println(response);
     * }
     * </pre>
     */
    ClassroomResponse updateTutors(String id, UpdateClassroomTutorRequest request);
}

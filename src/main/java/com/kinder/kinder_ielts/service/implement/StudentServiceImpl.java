package com.kinder.kinder_ielts.service.implement;
import com.kinder.kinder_ielts.constant.IsDelete;
import com.kinder.kinder_ielts.dto.request.student.CreateStudentRequest;
import com.kinder.kinder_ielts.dto.response.student.StudentResponse;
import com.kinder.kinder_ielts.entity.Account;
import com.kinder.kinder_ielts.entity.Student;
import com.kinder.kinder_ielts.mapper.ModelMapper;
import com.kinder.kinder_ielts.service.base.BaseAccountService;
import com.kinder.kinder_ielts.service.base.BaseStudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl {
    private final BaseStudentService baseStudentService;
    private final BaseAccountService baseAccountService;

    public StudentResponse createStudent(CreateStudentRequest request, String failMessage) {
        Student student = ModelMapper.map(request);
        Account account = baseAccountService.create(student.getAccount(), failMessage);
        student.setAccount(account);
        baseStudentService.saveStudent(student);
        Student thisStudent = baseStudentService.get(student.getId(), IsDelete.NOT_DELETED, failMessage);
        return StudentResponse.detail(thisStudent);
    }
}

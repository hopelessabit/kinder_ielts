package com.kinder.kinder_ielts.controller;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.classroom.CreateClassroomRequest;
import com.kinder.kinder_ielts.dto.request.course.CreateCourseRequest;
import com.kinder.kinder_ielts.dto.response.classroom.ClassroomResponse;
import com.kinder.kinder_ielts.dto.response.course.CourseResponse;
import com.kinder.kinder_ielts.dto.response.study_schedule.StudyScheduleResponse;
import com.kinder.kinder_ielts.repository.CourseRepository;
import com.kinder.kinder_ielts.response_message.ClassroomMessage;
import com.kinder.kinder_ielts.response_message.CourseMessage;
import com.kinder.kinder_ielts.response_message.StudyScheduleMessage;
import com.kinder.kinder_ielts.service.ClassroomService;
import com.kinder.kinder_ielts.service.base.BaseCourseService;
import com.kinder.kinder_ielts.service.implement.CourseServiceImpl;
import com.kinder.kinder_ielts.service.implement.OneDriveServiceImpl;
import com.kinder.kinder_ielts.service.implement.StudyScheduleServiceImpl;
import com.kinder.kinder_ielts.util.IdUtil;
import com.kinder.kinder_ielts.util.ResponseUtil;
import com.kinder.kinder_ielts.util.TimeZoneUtil;
import com.microsoft.graph.core.models.IProgressCallback;
import com.microsoft.graph.core.models.UploadResult;
import com.microsoft.graph.core.tasks.LargeFileUploadTask;
import com.microsoft.graph.drives.item.items.item.createlink.CreateLinkPostRequestBody;
import com.microsoft.graph.drives.item.items.item.createuploadsession.CreateUploadSessionPostRequestBody;
import com.microsoft.graph.models.*;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import com.microsoft.graph.users.item.sendmail.SendMailPostRequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

@RestController()
@Slf4j
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer")
public class TestController {
    private final PasswordEncoder passwordEncoder;
    private final BaseCourseService baseCourseService;
    private final ClassroomService classroomService;
    private final CourseServiceImpl courseService;
    private final StudyScheduleServiceImpl studyScheduleService;
    private final CourseRepository courseRepository;
    private final OneDriveServiceImpl oneDriveService;

    @GetMapping("/course/info/{id}")
    public ResponseEntity<ResponseData<CourseResponse>> getInfo(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> courseService.getInfo(id), CourseMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/course/detail/{id}")
    public ResponseEntity<ResponseData<CourseResponse>> getDetail(@PathVariable String id) {
        return ResponseUtil.getResponse(() -> courseService.getDetail(id), CourseMessage.FOUND_SUCCESSFULLY);
    }
    @GetMapping("/course/all")
    public ResponseEntity<ResponseData<List<CourseResponse>>> getAll() {
        return ResponseUtil.getResponse(() -> courseRepository.findAll().stream().map(CourseResponse::info).toList(), CourseMessage.NOT_FOUND);
    }

    @PostMapping("/course")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<CourseResponse>> createCourse(@RequestBody CreateCourseRequest request){
        return ResponseUtil.getResponse(() -> courseService.createCourse(request), CourseMessage.CREATED);
    }

    @GetMapping("/api/current-time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC+7")
    public ZonedDateTime getCurrentTime() {
        return ZonedDateTime.now();
    }

    // Receive ZonedDateTime from Frontend
    @PostMapping("/api/submit-time")
    public String receiveTime(@RequestBody ZonedDateTime clientTime) {
        log.info("Received Time: " + clientTime);
        return "Received time: " + clientTime;
    }

    @GetMapping("/get-encrypted-password/{password}")
    public String getEncryptedPassword(@PathVariable String password) {
        return passwordEncoder.encode(password);
    }

    @GetMapping("/str")
    public String str() {
        String a = TimeZoneUtil.getCurrentDateTime();
        return IdUtil.generateId();
    }

    @GetMapping("/study-schedule/all")
    public ResponseEntity<ResponseData<List<StudyScheduleResponse>>> get() {
        return ResponseUtil.getResponse(studyScheduleService::getAllInfo, StudyScheduleMessage.CREATED);
    }

    @PostMapping("/classroom/course/{courseId}")
    @SecurityRequirement(name = "Bearer")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ResponseEntity<ResponseData<ClassroomResponse>> create(@PathVariable String courseId, @RequestBody CreateClassroomRequest request) {
        return ResponseUtil.getResponse(() -> classroomService.createClassroom(courseId, request, ClassroomMessage.CREATE_FAILED), ClassroomMessage.CREATED);
    }

    @GetMapping("/send-mail")
    public ResponseEntity testSendMail(){
        final String siteId = "a151620.sharepoint.com,ebfabe2d-018c-409c-8968-ce2fd8478a39,039b0f2a-b864-4df1-8002-2b8348159e6f";
        final String driveId = "b!Lb7664wBnECJaM4v2EeKOSoPmwNkuPFNgAIrg0gVnm-5bK4ObU6pRIa3ZpYmjFEe";
        final String itemId = "01QLPIJMUBNY7PW2RGLJG335V5HPUNJBL6";
        TokenCredential tokenCredential = new ClientSecretCredentialBuilder()
                .tenantId("2ced3377-c996-45c8-bf26-d07bd60c5bbc")
                .clientId("a2621942-5d1d-4e45-94c4-db26eb7331c5")
                .clientSecret("EV98Q~MjOJYKG4r6kif3IMvUzIewfml1gch1GclN")
                .build();

        GraphServiceClient graphServiceClient = new GraphServiceClient(tokenCredential);

//        var a = graphServiceClient.sites().bySiteId(siteId);
//        var e = graphServiceClient.drives().byDriveId(driveId).items().byDriveItemId(itemId).children().get();

//        testSendMailFunc(graphServiceClient);
//        var a = graphServiceClient.users().get();
//        return ResponseEntity.ok(graphServiceClient);

        var b = graphServiceClient.drives().byDriveId(driveId).get();
        return ResponseEntity.ok(b);
    }

    @PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResponseData<String>> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("itemPath") String itemPath) {
        return ResponseUtil.getResponse(() -> oneDriveService.uploadFile(file, itemPath), "Upload file successfully");
    }

    // Create a Public Sharing Link
    public String createPublicShareLink(GraphServiceClient graphClient, String itemId) {
        try {
            CreateLinkPostRequestBody createLinkPostRequestBody = new CreateLinkPostRequestBody();
            createLinkPostRequestBody.setType("view");
            createLinkPostRequestBody.setScope("anonymous");

            Permission permissionResult = graphClient.drives()
                    .byDriveId("b!Lb7664wBnECJaM4v2EeKOSoPmwNkuPFNgAIrg0gVnm-5bK4ObU6pRIa3ZpYmjFEe")
                    .items()
                    .byDriveItemId(itemId)
                    .createLink()
                    .post(createLinkPostRequestBody);

            if (permissionResult != null && permissionResult.getLink() != null) {
                return permissionResult.getLink().getWebUrl();
            }
            else {
                return null;
            }

        } catch (Exception ex) {
            log.error("Error creating sharing link: " + ex.getMessage());
            return null;
        }
    }

    @PostMapping(value = "/upload-file1", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadFile1(@RequestParam("file") MultipartFile file,
                                             @RequestParam("itemPath") String itemPath) {
        try {
            final String siteId = "a151620.sharepoint.com,ebfabe2d-018c-409c-8968-ce2fd8478a39,039b0f2a-b864-4df1-8002-2b8348159e6f";
            final String driveId = "b!Lb7664wBnECJaM4v2EeKOSoPmwNkuPFNgAIrg0gVnm-5bK4ObU6pRIa3ZpYmjFEe";
            final String itemId = "01QLPIJMUBNY7PW2RGLJG335V5HPUNJBL6";
//            TokenCredential tokenCredential = new ClientSecretCredentialBuilder()
//                    .tenantId("2ced3377-c996-45c8-bf26-d07bd60c5bbc")
//                    .clientId("a2621942-5d1d-4e45-94c4-db26eb7331c5")
//                    .clientSecret("EV98Q~MjOJYKG4r6kif3IMvUzIewfml1gch1GclN")
//                    .build();
            TokenCredential tokenCredential = new ClientSecretCredentialBuilder()
                    .tenantId("")
                    .clientId("")
                    .clientSecret("")
                    .build();

            GraphServiceClient graphClient = new GraphServiceClient(tokenCredential);

            final InputStream fileStream = file.getInputStream();
            long streamSize = file.getSize();

// Set body of the upload session request
            CreateUploadSessionPostRequestBody uploadSessionRequest = new CreateUploadSessionPostRequestBody();
            DriveItemUploadableProperties properties = new DriveItemUploadableProperties();
            properties.getAdditionalData().put("@microsoft.graph.conflictBehavior", "replace");
            uploadSessionRequest.setItem(properties);

            UploadSession uploadSession = graphClient
                    .drives()
                    .byDriveId(driveId)
                    .items()
                    .byDriveItemId("root:/"+itemPath+":")
                    .createUploadSession()
                    .post(uploadSessionRequest);

// Create the upload task
            int maxSliceSize = 10 * 1024 * 1024;
            LargeFileUploadTask<DriveItem> largeFileUploadTask = new LargeFileUploadTask<>(
                    graphClient.getRequestAdapter(),
                    uploadSession,
                    fileStream,
                    streamSize,
                    maxSliceSize,
                    DriveItem::createFromDiscriminatorValue);

            int maxAttempts = 5;
// Create a callback used by the upload provider
            IProgressCallback callback = (current, max) -> log.info(
                    String.format("Uploaded %d bytes of %d total bytes", current, max));

            UploadResult<DriveItem> uploadResult = largeFileUploadTask.upload(maxAttempts, callback);
            if (uploadResult.isUploadSuccessful()) {
                log.info("Upload complete");
                log.info("Item ID: " + uploadResult.itemResponse.getId());
            } else {
                log.info("Upload failed");
            }
            return null;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    public void testSendMailFunc(GraphServiceClient graphServiceClient){
        SendMailPostRequestBody sendMailPostRequestBody = new SendMailPostRequestBody();
        Message message = new Message();
        message.setSubject("Hi em yêu");
        ItemBody itemBody = new ItemBody();
        itemBody.setContentType(BodyType.Text);
        itemBody.setContent("Chào em từ Minh app.");
        message.setBody(itemBody);

        LinkedList<Recipient> recipients = new LinkedList<>();
        Recipient recipient = new Recipient();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setAddress("micalminh1@gmail.com");
        recipient.setEmailAddress(emailAddress);
        recipients.add(recipient);
        message.setToRecipients(recipients);

        LinkedList<Recipient> ccRecipients = new LinkedList<Recipient>();
        Recipient recipient1 = new Recipient();
        EmailAddress emailAddress1 = new EmailAddress();
        emailAddress1.setAddress("micalminh1@gmail.com");
        recipient1.setEmailAddress(emailAddress1);
        ccRecipients.add(recipient1);
        message.setCcRecipients(ccRecipients);

        sendMailPostRequestBody.setMessage(message);
        sendMailPostRequestBody.setSaveToSentItems(false);
        graphServiceClient.users().byUserId("2af0bea4-8bae-49cb-b98b-850948f1de33").sendMail().post(sendMailPostRequestBody);
    }


    @GetMapping("/get-item-id/{itemId}")
    public ResponseEntity getItemId(@PathVariable String itemId) {
        return ResponseUtil.getResponse(() -> oneDriveService.getImage(itemId), "Get item id successfully");
    }
}

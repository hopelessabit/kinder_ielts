package com.kinder.kinder_ielts.service.implement;

import com.azure.core.credential.TokenCredential;
import com.azure.identity.ClientSecretCredentialBuilder;
import com.microsoft.graph.core.models.IProgressCallback;
import com.microsoft.graph.core.models.UploadResult;
import com.microsoft.graph.core.tasks.LargeFileUploadTask;
import com.microsoft.graph.drives.item.items.item.createlink.CreateLinkPostRequestBody;
import com.microsoft.graph.drives.item.items.item.createuploadsession.CreateUploadSessionPostRequestBody;
import com.microsoft.graph.models.DriveItem;
import com.microsoft.graph.models.DriveItemUploadableProperties;
import com.microsoft.graph.models.Permission;
import com.microsoft.graph.models.UploadSession;
import com.microsoft.graph.serviceclient.GraphServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OneDriveServiceImpl {
    @Value("${onedrive.driveId}")
    private String driveId;
    private final GraphServiceClient graphClient;

    public String uploadFile(MultipartFile file, String itemPath) {
        try {
            String originalFilename = file.getOriginalFilename();

            itemPath += "/" + originalFilename;
            final InputStream fileStream = file.getInputStream();
            long streamSize = file.getSize();

// Set body of the upload session request
            CreateUploadSessionPostRequestBody uploadSessionRequest = new CreateUploadSessionPostRequestBody();
            DriveItemUploadableProperties properties = new DriveItemUploadableProperties();
            properties.getAdditionalData().put("@microsoft.graph.conflictBehavior", "replace"); // Use additionalDataManager for put
            uploadSessionRequest.setItem(properties);

            UploadSession uploadSession = graphClient
                    .drives()
                    .byDriveId(driveId)
                    .items()
                    .byDriveItemId("root:/" + itemPath + ":")
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
                String downloadLink = createPublicShareLink(uploadResult.itemResponse.getId());
                if (downloadLink != null) {
                    return downloadLink + "?download=1";
                }
                else
                    return "Failed to create a public sharing link.";


            } else {
                log.info("Upload failed");
                return "Upload failed";
            }
        } catch (Exception e) {
            log.error("Error during file upload: ", e); // Log the exception for debugging.
            throw new RuntimeException("Error during file upload: ", e);
        }
    }

    // Create a Public Sharing Link
    public String createPublicShareLink(String itemId) {
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
            log.info("Link =============================: {}", permissionResult.getLink().getWebUrl());

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
    public Permission createPublicShareLinkTest(String itemId) {
        try {
            CreateLinkPostRequestBody createLinkPostRequestBody = new CreateLinkPostRequestBody();
            createLinkPostRequestBody.setType("view");
            createLinkPostRequestBody.setScope("anonymous");
            OffsetDateTime time = OffsetDateTime.now().plusYears(1000);
            createLinkPostRequestBody.setExpirationDateTime(time);


            Permission permissionResult = graphClient.drives()
                    .byDriveId("b!Lb7664wBnECJaM4v2EeKOSoPmwNkuPFNgAIrg0gVnm-5bK4ObU6pRIa3ZpYmjFEe")
                    .items()
                    .byDriveItemId(itemId)
                    .createLink()
                    .post(createLinkPostRequestBody);

            if (permissionResult != null && permissionResult.getLink() != null) {
                return permissionResult;
            }
            else {
                return null;
            }

        } catch (Exception ex) {
            log.error("Error creating sharing link: " + ex.getMessage());
            return null;
        }
    }

    public Object getImage(String itemId) {
        try {
//            DriveItem driveItem = graphClient.drives()
//                    .byDriveId("b!Lb7664wBnECJaM4v2EeKOSoPmwNkuPFNgAIrg0gVnm-5bK4ObU6pRIa3ZpYmjFEe")
//                    .items()
//                    .byDriveItemId(itemId)
//                    .get();
//            String a = driveItem.getWebUrl();
//            byte[] img = graphClient.drives()
//                    .byDriveId("b!Lb7664wBnECJaM4v2EeKOSoPmwNkuPFNgAIrg0gVnm-5bK4ObU6pRIa3ZpYmjFEe")
//                    .items()
//                    .byDriveItemId(itemId)
//                    .get().getContent();

            CreateLinkPostRequestBody createLinkPostRequestBody = new CreateLinkPostRequestBody();
            createLinkPostRequestBody.setType("view");
            createLinkPostRequestBody.setScope("anonymous");
            OffsetDateTime time = OffsetDateTime.now().plusYears(1000);
            createLinkPostRequestBody.setExpirationDateTime(time);


            Permission permissionResult = graphClient.drives()
                    .byDriveId("b!Lb7664wBnECJaM4v2EeKOSoPmwNkuPFNgAIrg0gVnm-5bK4ObU6pRIa3ZpYmjFEe")
                    .items()
                    .byDriveItemId(itemId)
                    .createLink()
                    .post(createLinkPostRequestBody);
            return permissionResult;
        } catch (Exception ex) {
            log.error("Error getting direct image link: " + ex.getMessage());
        }
        return null;
    }

    public DriveItem getById(String itemId) {
        CreateLinkPostRequestBody createLinkPostRequestBody = new CreateLinkPostRequestBody();
        createLinkPostRequestBody.setType("view");
        createLinkPostRequestBody.setScope("anonymous");
        return graphClient.drives()
                .byDriveId("b!Lb7664wBnECJaM4v2EeKOSoPmwNkuPFNgAIrg0gVnm-5bK4ObU6pRIa3ZpYmjFEe")
                .items()
                .byDriveItemId(itemId)
                .get();
    }

}

package com.kinder.kinder_ielts.controller;

import com.kinder.kinder_ielts.dto.ResponseData;
import com.kinder.kinder_ielts.dto.request.material_link.UpdateMaterialLinkRequest;
import com.kinder.kinder_ielts.dto.response.material_link.MaterialLinkResponse;
import com.kinder.kinder_ielts.response_message.MaterialLinkMessage;
import com.kinder.kinder_ielts.service.implement.MaterialLinkServiceImpl;
import com.kinder.kinder_ielts.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/material-links")
@SecurityRequirement(name = "Bearer")
@RequiredArgsConstructor
public class MaterialLinkController {
    private final MaterialLinkServiceImpl materialLinkService;

    @PutMapping("/{materialLinkId}")
    @Operation(summary = "Update a material link")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<MaterialLinkResponse>> updateMaterialLink(@PathVariable String materialLinkId, @RequestBody UpdateMaterialLinkRequest request) {
        return ResponseUtil.getResponse(() -> materialLinkService.updateMaterialLink(materialLinkId, request, MaterialLinkMessage.INFO_UPDATE_FAILED), MaterialLinkMessage.INFO_UPDATED);
    }

    @GetMapping("/info/{materialLinkId}")
    @Operation(summary = "Get a material link's info")
    public ResponseEntity<ResponseData<MaterialLinkResponse>> getInfo(@PathVariable String materialLinkId, @RequestParam(defaultValue = "false") Boolean includeForAdmin) {
        return ResponseUtil.getResponse(() -> materialLinkService.get(materialLinkId, includeForAdmin, MaterialLinkMessage.NOT_FOUND), MaterialLinkMessage.FOUND_SUCCESSFULLY);
    }

    @GetMapping("/detail/{materialLinkId}")
    @Operation(summary = "Get a material link's detail")
    public ResponseEntity<ResponseData<MaterialLinkResponse>> getDetail(@PathVariable String materialLinkId, @RequestParam(defaultValue = "false") Boolean includeForAdmin) {
        return ResponseUtil.getResponse(() -> materialLinkService.get(materialLinkId, includeForAdmin, MaterialLinkMessage.NOT_FOUND), MaterialLinkMessage.FOUND_SUCCESSFULLY);
    }

    @DeleteMapping("/{materialLinkId}")
    @Operation(summary = "Delete a material link")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<Void>> delete(@PathVariable String materialLinkId) {
        return ResponseUtil.getResponse(() -> materialLinkService.delete(materialLinkId, MaterialLinkMessage.DELETE_FAILED), MaterialLinkMessage.IS_DELETED);
    }

    @PatchMapping("/{materialLinkId}/view-status")
    @Operation(summary = "Update a material link's view status")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR','TUTOR')")
    public ResponseEntity<ResponseData<MaterialLinkResponse>> updateViewStatus(@PathVariable String materialLinkId) {
        return ResponseUtil.getResponse(() -> materialLinkService.updateViewStatus(materialLinkId, MaterialLinkMessage.VIEW_STATUS_UPDATE_FAILED), MaterialLinkMessage.VIEW_STATUS_UPDATED);
    }
}

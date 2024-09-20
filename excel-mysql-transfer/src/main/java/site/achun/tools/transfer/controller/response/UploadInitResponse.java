package site.achun.tools.transfer.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * upload file（excel or csv） information
 * &#064;Author Heiffeng
 * &#064;Date 2024-09-20 14:00:06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadInitResponse {

    private String fileName;
    private List<String> headers;

}

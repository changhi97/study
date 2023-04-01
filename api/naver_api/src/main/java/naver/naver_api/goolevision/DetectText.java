package naver.naver_api.goolevision;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class DetectText {
    public void detectText() throws IOException {
        // TODO(developer): Replace these variables before running the sample.
        String filePath = "/Users/kimchanghui/Desktop/studyRepository/befc5512-b001-4506-8af1-ae8ba968b178.png";
        detectText(filePath);
    }

    // Detects text in the specified image.
    public Optional<String> detectText(String filePath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();
        String result =null;

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    break;
                }
//
//         For full list of available annotations, see http://g.co/cloud/vision/docs
//        for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
//          System.out.format("Text: %s%n", annotation.getDescription());
//          System.out.format("Position : %s%n", annotation.getBoundingPoly());
//        }
                System.out.println(res.getTextAnnotationsList().get(0).getDescription());
                result = res.getTextAnnotationsList().get(0).getDescription();
            }
        }
        return Optional.ofNullable(result);
    }
}
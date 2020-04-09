package com.lattice.SecurePrint;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import javax.print.*;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class SecurePrintController {

    @GetMapping("/print")
    public String print() throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        //Make Http call to Download resource
        HttpGet request = new HttpGet(
                "http:localhost:8080/download/resume.pdf");

        HttpResponse response = client.execute(request);
        HttpEntity entity = response.getEntity();

        int responseCode = response.getStatusLine().getStatusCode();
        // get Input Stream from resource.
        InputStream is = entity.getContent();
        if(responseCode== 200) {
            // Get Available Printer Object
            PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
            if (printService != null) {
                DocPrintJob job = printService.createPrintJob();
                //Initialize Document with Input Stream
                Doc myDoc = new SimpleDoc(is, DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                try {
                    // Issue Print command to printer
                    job.print(myDoc, null);
                } catch (PrintException e) {
                    e.printStackTrace();
                }
            }
            return "Success";
        }
        return "failed";
    }
}

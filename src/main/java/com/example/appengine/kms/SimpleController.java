/*
 * Copyright 2022 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.appengine.kms;

import com.google.cloud.kms.v1.CryptoKey;
import com.google.cloud.kms.v1.CryptoKeyName;
import com.google.cloud.kms.v1.EkmServiceClient;
import com.google.cloud.kms.v1.KeyManagementServiceClient;
import com.google.cloud.kms.v1.KeyManagementServiceClient.ListKeyRingsPagedResponse;
import com.google.cloud.kms.v1.KeyRing;
import com.google.cloud.kms.v1.KeyRingName;
import com.google.cloud.kms.v1.ListCryptoKeysRequest;
import com.google.cloud.kms.v1.LocationName;
import com.google.cloud.secretmanager.v1.ProjectName;
import com.google.cloud.secretmanager.v1.Replication;
import com.google.cloud.secretmanager.v1.Replication.Automatic;
import com.google.cloud.secretmanager.v1.Secret;
import com.google.iam.v1.GetIamPolicyRequest;
import com.google.iam.v1.GetPolicyOptions;
import com.google.iam.v1.Policy;
import com.google.iam.v1.TestIamPermissionsRequest;
import com.google.iam.v1.TestIamPermissionsResponse;
import java.io.IOException;
import java.util.ArrayList;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient.ListSecretVersionsPagedResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient.ListSecretsPagedResponse;

@Controller
public class SimpleController {

    static String projectId = "ludo-in-in";
    static String locationId = "us-central1";
    static String keyRing = "ludokeyring1";

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String home(Model model) {
        return "Hello Ludo";
    }

    @GetMapping(value = "/l", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String homel(Model model) throws IOException {
        try ( SecretManagerServiceClient secretManagerServiceClient
                = SecretManagerServiceClient.create()) {
            ProjectName parent = ProjectName.of(projectId);
            String secretId = "secretId" + System.currentTimeMillis();
            Replication rep = Replication.newBuilder().setAutomatic(Automatic.getDefaultInstance()).build();
            Secret secret = Secret.newBuilder().setEtag("ludotag").setReplication(rep).build();

            Secret response = secretManagerServiceClient.createSecret(parent, secretId, secret);
            String l = " all secrets: ";
            ListSecretsPagedResponse resp = secretManagerServiceClient.listSecrets(parent);
            for (Secret kr : resp.iterateAll()) {
                l = l + " " + kr.getName();
            }
            return "Hello Ludo created secret:" + response.getName() + " all list is = " + l;
        }
    }

    @GetMapping(value = "/ListKeys", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String ListKeys(Model model) {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        String res = "Key rings are: ";
        try ( KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the parent from the project and location.
            LocationName parent = LocationName.of(projectId, locationId);

            // Call the API.
            ListKeyRingsPagedResponse response = client.listKeyRings(parent);

            // Iterate over each key ring and print its name.
            System.out.println("key rings ludo : " + response);
            for (KeyRing kr : response.iterateAll()) {
                System.out.println("key rings lis : " + kr);
                res = res + " " + kr.getName();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR key rings  : " + res);

        }
        System.out.println("ALL key rings  : " + res);
        return res;

    }

    @GetMapping(value = "/syncgetiampolicy", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public static String syncGetIamPolicy(Model model) throws Exception {
        // This snippet has been automatically generated and should be regarded as a code template only.
        // It will require modifications to work:
        // - It may require correct/in-range values for request initialization.
        // - It may require specifying regional endpoints when creating the service client as shown in
        // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
        try ( EkmServiceClient ekmServiceClient = EkmServiceClient.create()) {
            GetIamPolicyRequest request
                    = GetIamPolicyRequest.newBuilder()
                            .setResource(
                                    CryptoKeyName.of(projectId, locationId, keyRing, "[CRYPTO_KEY]")
                                            .toString())
                            .setOptions(GetPolicyOptions.newBuilder().build())
                            .build();
            Policy response = ekmServiceClient.getIamPolicy(request);
        }
        return "success";

    }

    @GetMapping(value = "/synctestiampermissions", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public static String syncTestIamPermissions(Model model) throws Exception {
        // This snippet has been automatically generated and should be regarded as a code template only.
        // It will require modifications to work:
        // - It may require correct/in-range values for request initialization.
        // - It may require specifying regional endpoints when creating the service client as shown in
        // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
        try ( EkmServiceClient ekmServiceClient = EkmServiceClient.create()) {
            TestIamPermissionsRequest request
                    = TestIamPermissionsRequest.newBuilder()
                            .setResource(
                                    CryptoKeyName.of(projectId, locationId, keyRing, "[CRYPTO_KEY]")
                                            .toString())
                            .addAllPermissions(new ArrayList<>())
                            .build();
            TestIamPermissionsResponse response = ekmServiceClient.testIamPermissions(request);
        }
        return "success";
    }

    @GetMapping(value = "/synclistcryptokeys", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public static String syncListCryptoKeys(Model model) throws Exception {
        // This snippet has been automatically generated and should be regarded as a code template only.
        // It will require modifications to work:
        // - It may require correct/in-range values for request initialization.
        // - It may require specifying regional endpoints when creating the service client as shown in
        // https://cloud.google.com/java/docs/setup#configure_endpoints_for_the_client_library
        try ( KeyManagementServiceClient keyManagementServiceClient
                = KeyManagementServiceClient.create()) {
            ListCryptoKeysRequest request
                    = ListCryptoKeysRequest.newBuilder()
                            .setParent(KeyRingName.of(projectId, locationId, keyRing).toString())
                            .setPageSize(883849137)
                            .setPageToken("pageToken873572522")
                            .setFilter("filter-1274492040")
                            .setOrderBy("orderBy-1207110587")
                            .build();
            for (CryptoKey element : keyManagementServiceClient.listCryptoKeys(request).iterateAll()) {
                // doThingsWith(element);
            }
        }
        return "success";

    }

    @GetMapping(value = "/createkeyring", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String createKeyRing(Model model) throws IOException {
        // TODO(developer): Replace these variables before running the sample.

        String id = "my-asymmetric-signing-key";
        createKeyRing(projectId, locationId, id);
        return "success";

    }

    // Create a new key ring.
    public void createKeyRing(String projectId, String locationId, String id) throws IOException {
        // Initialize client that will be used to send requests. This client only
        // needs to be created once, and can be reused for multiple requests. After
        // completing all of your requests, call the "close" method on the client to
        // safely clean up any remaining background resources.
        try ( KeyManagementServiceClient client = KeyManagementServiceClient.create()) {
            // Build the parent name from the project and location.
            LocationName locationName = LocationName.of(projectId, locationId);

            // Build the key ring to create.
            KeyRing lkeyRing = KeyRing.newBuilder().build();

            // Create the key ring.
            KeyRing createdKeyRing = client.createKeyRing(locationName, id, lkeyRing);
            System.out.printf("Created key ring %s%n", createdKeyRing.getName());
        }
    }
}

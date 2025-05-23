package org.alfresco.rest.renditions;

import java.time.Duration;

import com.google.common.base.Predicates;
import org.awaitility.Awaitility;
import org.awaitility.Durations;
import org.springframework.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import org.alfresco.rest.RestTest;
import org.alfresco.rest.core.RestResponse;
import org.alfresco.rest.model.RestNodeModel;
import org.alfresco.utility.Utility;
import org.alfresco.utility.model.FileModel;
import org.alfresco.utility.model.FolderModel;
import org.alfresco.utility.model.SiteModel;
import org.alfresco.utility.model.UserModel;

public abstract class RenditionIntegrationTests extends RestTest
{

    protected UserModel user;
    protected SiteModel site;

    @BeforeClass(alwaysRun = true)
    public void dataPreparation() throws Exception
    {
        user = dataUser.createRandomTestUser();
        site = dataSite.usingUser(user).createPublicRandomSite();
    }

    /**
     * Check that a rendition can be created for the specified node id
     * 
     * @param fileName
     * @param nodeId
     * @param renditionId
     * @param expectedMimeType
     * @throws Exception
     */
    protected void checkRendition(String fileName, String nodeId, String renditionId, String expectedMimeType) throws Exception
    {
        FileModel file = new FileModel();
        file.setNodeRef(nodeId);

        // 1. Preemptively delete an existing rendition of the file using RESTAPI
        restClient.withCoreAPI().usingNode(file).deleteNodeRendition(renditionId);

        // 2. Create a rendition of the file using RESTAPI
        restClient.withCoreAPI().usingNode(file).createNodeRendition(renditionId);
        Assert.assertEquals(Integer.valueOf(restClient.getStatusCode()).intValue(), HttpStatus.ACCEPTED.value(),
                "Failed to submit a request for rendition. [" + fileName + ", " + nodeId + ", " + renditionId + "] [source file, node ID, rendition ID]");

        // 3. Verify that a rendition of the file is created and has content using RESTAPI
        RestResponse restResponse = restClient.withCoreAPI().usingNode(file).getNodeRenditionContentUntilIsCreated(renditionId);
        Assert.assertEquals(Integer.valueOf(restClient.getStatusCode()).intValue(), HttpStatus.OK.value(),
                "Failed to produce rendition. [" + fileName + ", " + nodeId + ", " + renditionId + "] [source file, node ID, rendition ID]");

        // 4. Check the returned content type
        Assert.assertEquals(restClient.getResponseHeaders().getValue("Content-Type"), expectedMimeType + ";charset=UTF-8",
                "Rendition was created but it has the wrong Content-Type. [" + fileName + ", " + nodeId + ", " + renditionId + "] [source file, node ID, rendition ID]");

        Assert.assertTrue((restResponse.getResponse().body().asInputStream().available() > 0),
                "Rendition was created but its content is empty. [" + fileName + ", " + nodeId + ", " + renditionId + "] [source file, node ID, rendition ID]");
    }

    /**
     * Upload a file and return its node id
     * 
     * @param sourceFile
     * @return
     * @throws Exception
     */
    protected RestNodeModel uploadFile(String sourceFile) throws Exception
    {
        FolderModel folder = Awaitility
                .await()
                .atMost(Duration.ofSeconds(30))
                .pollInterval(Durations.ONE_SECOND)
                .ignoreExceptions()
                .until(() -> {
                    FolderModel randomFolderModel = FolderModel.getRandomFolderModel();
                    return dataContent.usingUser(user).usingSite(site).createFolder(randomFolderModel);
                }, Predicates.notNull());
        restClient.authenticateUser(user).configureRequestSpec()
                .addMultiPart("filedata", Utility.getResourceTestDataFile(sourceFile));
        RestNodeModel fileNode = restClient.authenticateUser(user).withCoreAPI().usingNode(folder).createNode();

        Assert.assertEquals(Integer.valueOf(restClient.getStatusCode()).intValue(), HttpStatus.CREATED.value(),
                "Failed to created a node for rendition tests using file " + sourceFile);

        return fileNode;
    }

}

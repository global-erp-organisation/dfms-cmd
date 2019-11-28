package com.expedia.content.media.processing.pipeline.archive;

import java.net.URL;

/**
 * Moves images files when for rejection. Does the same thing as 
 * {@link com.expedia.content.media.processing.pipeline.archive.FolderArchivingProcess}
 * but has a @{link com.expedia.content.media.processing.metrics.annotations.Meter} for 
 * the archiveSourceImage method.
 */
public class RejectedFolderArchivingProcess extends FolderArchivingProcess {
    
    private static final String REJECT_PURPOSE = "reject";
    
    public RejectedFolderArchivingProcess(String rootPath) {
        super(rootPath);
    }
    
    @Override
    protected String getPurpose() {
        return REJECT_PURPOSE;
    }

    @Override
    public void archiveSourceImage(URL file, String supplementalPath) throws ArchiveImageException {
        super.archiveSourceImage(file, supplementalPath);
    }
}

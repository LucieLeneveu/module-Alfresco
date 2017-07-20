package org.cd76.platformsample;

import java.util.List;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;

public class FolderCreationFromConsultationCreationRule extends ActionExecuterAbstractBase {

	private FileFolderService fileFolderService;
	
	public void setFileFolderService(FileFolderService fileFolderService) {
        this.fileFolderService = fileFolderService;
    }
	
	@Override
	protected void executeImpl(Action ruleAction, NodeRef folderNodeRef) {
		FileInfo folderInformations = fileFolderService.getFileInfo(folderNodeRef);
		if (folderInformations.isFolder()) {
			fileFolderService.create(folderNodeRef, "Préparation", ContentModel.TYPE_FOLDER);
			fileFolderService.create(folderNodeRef, "Réception", ContentModel.TYPE_FOLDER);
			fileFolderService.create(folderNodeRef, "Examen des offres", ContentModel.TYPE_FOLDER);
			fileFolderService.create(folderNodeRef, "Recours", ContentModel.TYPE_FOLDER);
			fileFolderService.create(folderNodeRef, "Corbeille", ContentModel.TYPE_FOLDER);
		}
	}

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> arg0) {
	}

}

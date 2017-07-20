package org.cd76.platformsample;

import java.util.List;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.ParameterDefinitionImpl;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.dictionary.DataTypeDefinition;
import org.alfresco.service.cmr.model.FileExistsException;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.NodeRef;

import com.ibm.icu.util.Calendar;

public class ClassementPlanFromDateOfCreationRule extends ActionExecuterAbstractBase {
	
	private static final String PARAM_DESTINATION_FOLDER = "destination-folder";
	
	private FileFolderService fileFolderService;
	
	public void setFileFolderService(FileFolderService fileFolderService) {
        this.fileFolderService = fileFolderService;
    }

	@Override
	protected void addParameterDefinitions(List<ParameterDefinition> paramList) {	
		paramList.add(
	        	new ParameterDefinitionImpl(PARAM_DESTINATION_FOLDER,
	        								DataTypeDefinition.NODE_REF,
	        								true,
	        								getParamDisplayLabel(PARAM_DESTINATION_FOLDER)));
	}

	@Override
	protected void executeImpl(Action ruleAction, NodeRef fileNodeRef) {
		FileInfo fileInformations = fileFolderService.getFileInfo(fileNodeRef);
		if (!fileInformations.isFolder()) {
			Calendar calendar = Calendar.getInstance();
			int yearOfDate = calendar.get(Calendar.YEAR);
			int monthOfDate = calendar.get(Calendar.MONTH) + 1;
			int dayOfDate = calendar.get(Calendar.DAY_OF_MONTH);
			NodeRef destinationParent = (NodeRef) ruleAction.getParameterValue(PARAM_DESTINATION_FOLDER);
			NodeRef yearFolder = fileFolderService.searchSimple(destinationParent, String.valueOf(yearOfDate));
			if (yearFolder == null) {
				FileInfo yearInformations = fileFolderService.create(destinationParent, String.valueOf(yearOfDate), ContentModel.TYPE_FOLDER);
				yearFolder = yearInformations.getNodeRef();
			}
			NodeRef monthFolder = fileFolderService.searchSimple(yearFolder, String.valueOf(monthOfDate));
			if (monthFolder == null) {
				FileInfo monthInformations = fileFolderService.create(yearFolder, String.valueOf(monthOfDate), ContentModel.TYPE_FOLDER);
				monthFolder = monthInformations.getNodeRef();
			}
			NodeRef dayFolder = fileFolderService.searchSimple(monthFolder, String.valueOf(dayOfDate));
			if (dayFolder == null) {
				FileInfo dayInformations = fileFolderService.create(monthFolder, String.valueOf(dayOfDate), ContentModel.TYPE_FOLDER);
				dayFolder = dayInformations.getNodeRef();
			}
			try {
				fileFolderService.move(fileNodeRef, dayFolder, null);
			} catch (FileExistsException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}

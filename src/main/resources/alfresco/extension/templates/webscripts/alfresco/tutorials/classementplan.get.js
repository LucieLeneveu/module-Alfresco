
// On récupère le paramètre et on encode les parties qui doivent l'être
var fieldPath = args['path'].split("/");
var encodePath = new Array();
// encodePath[0] contient le chemin encodé vers le répertoire 
encodePath[0] = "/";
// encodePath[1] contient le chemin encodé vers le parent du répertoire
encodePath[1] = "/";
// encodePath[2] contient le nom encodé du répertoire
encodePath[2] = "";
for (var i = 0; i < fieldPath.length; ++i) {
	if (fieldPath[i].indexOf("cm") != -1) {
		var name = fieldPath[i].split(":");
		if (i == fieldPath.length - 1) {
			encodePath[2] += search.ISO9075Encode(name[name.length - 1]);
		} else {
			encodePath[1] += "cm:" + search.ISO9075Encode(name[name.length - 1]) + "/";
		}
		encodePath[0] += "cm:" + search.ISO9075Encode(name[name.length - 1]) + "/";
	} else {
		if (i == fieldPath.length - 1) {
			encodePath[2] += fieldPath[i].split(":")[1];
		} else {
			encodePath[1] += fieldPath[i] + "/";
		}
		encodePath[0] += fieldPath[i] + "/";
	}
}
encodePath[0] += "/*";
encodePath[1] += "/*";

//Permet de récupérer le répertoire 
path = "PATH:\"" + encodePath[1] + "\"" + " AND @name:\""+ encodePath[2] + "\"";
def =
{
    query: path,
    language: "lucene"
};
var nodeParent = search.query(def)[0];

// Permet de récupérer tous les fichiers du répertoire à réorganiser
var path = "PATH:\"" + encodePath[0] + "\"" + " AND TYPE:\"cm:content\"";
var max = 500;
var paging = 
{
	maxItems: max,
	skipCount:0
};
var def =
{
    query: path,
    language: "lucene",
	page: paging
};
var files = search.query(def);
model.file = 0;
var skip = 0;
while (files.length != 0) {

	// On parcours tous les fichiers
	for (var i = 0; i < files.length; i++) {
		// On récupère la date de création
		var creationDate = files[i].properties["cm:created"];
		var yearOfDate = creationDate.getFullYear();
		var monthOfDate = creationDate.getMonth() + 1;
		var dayOfDate = creationDate.getDate();
		
		// On récupère le répertoire de l'année 
		var yearFolder = nodeParent.childByNamePath(yearOfDate);
		// Si le répertoire n'existe pas, on le crée
		if (yearFolder == null) {
			yearFolder = nodeParent.createFolder(yearOfDate);
		}
		
		// On récupère le répertoire du mois 
		var monthFolder = yearFolder.childByNamePath(monthOfDate);
		// Si le répertoire n'existe pas, on le crée
		if (monthFolder == null) {
			monthFolder = yearFolder.createFolder(monthOfDate);
		}
		
		
		// On récupère le répertoire du jour 
		var dayFolder = monthFolder.childByNamePath(dayOfDate);
		// Si le répertoire n'existe pas, on le crée
		if (dayFolder == null) {
			dayFolder = monthFolder.createFolder(dayOfDate);
		}
		
		// On déplace le fichier
		files[i].move(dayFolder);
		dayFolder.save();
	}
	model.file += files.length;
	skip += max;
	paging = 
	{
		maxItems: max,
		skipCount:skip
	};
	var def =
	{
		query: path,
		language: "lucene",
		page: paging
	};
	files = search.query(def);
}
model.path = args['path'];
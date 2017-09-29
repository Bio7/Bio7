import static com.eco.bio7.rbridge.RServeUtil.*;
import org.rosuda.REngine.REXP;
import org.apache.commons.io.FilenameUtils;
/*
Gridfile importer script. Using the 'rgdal' package of R.
Author: M. Austenfeld 
Year:   2005-2017

 */

evalR(".isInstalled<-as.character(require('rgdal'))", null);
rexpr = fromR(".isInstalled");
isAvail = rexpr.asString();

if (isAvail.equals("FALSE")) {
	Bio7Dialog.message("Can't load 'rgdal' package!");
	return;
}

files = Bio7Dialog.openFile();

if (files != null) {
	if (Bio7Dialog.getOS().equals("Windows")) {
		files = files.replace("\\", "/");
	}
	f = new File(files);
	name = FilenameUtils.removeExtension(f.getName());

	//c.assign(".fileGdal", f.toString());
	toR(".fileGdalBio7", f.toString());
	/*Read the gridfile with the filename as the layer!*/
	evalR("try(" + name + " <- readGDAL(.fileGdalBio7));", null);
	System.out.println("Loaded Grid: " + name + "\n");
	evalR("try(print(summary(" + name + ")));try(remove(list = c('.fileGdalBio7','.isInstalled')));", null);

}
	


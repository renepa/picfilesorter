package de.repa.filesorter.files;

import de.repa.filesorter.files.model.ValidFileName;

import java.util.List;

public interface FileLoadClient {
    public List<ValidFileName> loadValidFileNamesOfPicturesInRootDir();
}

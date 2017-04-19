package de.repa.supracam.files;

import de.repa.supracam.files.model.ValidFileName;

import java.util.List;

public interface FileLoadClient {
    public List<ValidFileName> loadValidFileNamesOfPicturesInRootDir();
}

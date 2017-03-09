package de.repa.supracam.infrastructure;

import de.repa.supracam.files.model.ValidFileName;

import java.util.List;

public interface FileLoadClient {
    public List<ValidFileName> loadNamesOfPicturesInRootDir();
}

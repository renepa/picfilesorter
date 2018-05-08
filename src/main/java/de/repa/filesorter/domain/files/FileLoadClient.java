package de.repa.filesorter.domain.files;

import java.util.List;

public interface FileLoadClient {
    public List<ValidFileName> loadValidFileNamesOfPicturesInRootDir();
}

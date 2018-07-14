import groovy.io.FileType

import java.nio.file.Files


class CopyMapping {
    void main(String[] args) {
        File metaInf = new File("META-INF")
        File stubsPath = new File("target/stubs")
        File mappingsPath
        new File(stubsPath.path, "META-INF").traverse(maxDepth: 3, type:FileType.DIRECTORIES) {it -> mappingsPath = it }
        new AntBuilder().copy( todir: metaInf.path ) {
            fileset( dir: mappingsPath.path )
        }
        metaInf.eachFileRecurse(FileType.FILES) {
            if(it.name.endsWith('.json')) {
                File source = it;
                File mappingsTargetPath = new File(it.parentFile.parentFile.path, "mappings")
                Files.createDirectories(mappingsTargetPath.toPath())
                println mappingsTargetPath
                new AntBuilder().move( todir: mappingsTargetPath.path ) {
                    fileset( file: source.path )
                }
            }
        }
    }
}
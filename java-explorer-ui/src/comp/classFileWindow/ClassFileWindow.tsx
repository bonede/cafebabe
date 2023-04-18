import './ClassFileWindow.css'
import {MosaicPath, MosaicWindow} from "react-mosaic-component";
import React, {useState} from "react";
import {ClassFile} from "../../api/ApiClient";
import {Button, Tab, Tabs} from "@blueprintjs/core";
import {ClassImageView} from "./ClassImageView";
import {TitleBar} from "../titleBar/TitleBar";
import {saveZipFile} from "../Utils";

export interface ClassFileWindowProps{
    mosaicPath: MosaicPath,
    classFiles: ClassFile[]
    selectedLines?: number[]
    selectedFile?: string
    onSelectLine?: (file: string, line?: number) => void
}

export const ClassFileWindow = (props: ClassFileWindowProps) => {
    const [classFileIndex, setClassFileIndex] = useState(0)
    const handleTabChange = (i: number) => {
        setClassFileIndex(i)
    }
    const tabs = <Tabs id="class-file-tabs"
                       onChange={handleTabChange}
                       selectedTabId={classFileIndex}
    >
        {props.classFiles && props.classFiles.map((classFile, i) => <Tab key={i + ""} id={i} title={classFile.path} />)}
    </Tabs>
    const handleSaveClick = () => {
        if(props.classFiles == null || props.classFiles.length == 0){
            return
        }
        saveZipFile("classes", props.classFiles)
    }
    const actionButtons = <div>
        <Button title={"Save class file(s)"} onClick={handleSaveClick} minimal={true} icon="download" />
    </div>

    return <MosaicWindow<string>
        path={props.mosaicPath}
        title="Output"
        renderToolbar={() =><div style={{width: "100%"}}> <TitleBar title={tabs} actions={actionButtons} /> </div> }

    >
        {
            props.classFiles[classFileIndex] && <div className="class-image-window-content">
                {
                    // TODO handle file name
                }
                <ClassImageView onSelectLine={props.onSelectLine} file={props.classFiles[classFileIndex].path} selectedLines={props.selectedLines} classImage={props.classFiles[classFileIndex].classImage} />
            </div>
        }

    </MosaicWindow>
}
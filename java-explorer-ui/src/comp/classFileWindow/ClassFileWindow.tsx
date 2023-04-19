import './ClassFileWindow.css'
import React, {useState} from "react";
import {ClassFile} from "../../api/ApiClient";
import {Button, Tab, Tabs} from "@blueprintjs/core";
import {ClassImageView} from "./ClassImageView";
import {saveZipFile} from "../Utils";
import {AppWindow} from "../window/AppWindow";

export interface ClassFileWindowProps{
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

    return <AppWindow
        title={tabs}
        actions={actionButtons}
    >
        {
            props.classFiles[classFileIndex] && <div className="class-image-window-content">
                {
                    // TODO handle file name
                }
                <ClassImageView onSelectLine={props.onSelectLine} file={props.classFiles[classFileIndex].path} selectedLines={props.selectedLines} classImage={props.classFiles[classFileIndex].classImage} />
            </div>
        }

    </AppWindow>
}
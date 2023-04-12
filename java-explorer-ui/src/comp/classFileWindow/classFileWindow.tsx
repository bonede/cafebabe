import './classFileWindow.css'
import {MosaicPath, MosaicWindow} from "react-mosaic-component";
import React, {useState} from "react";
import {ClassFile} from "../../api/ApiClient";
import {Tab, Tabs} from "@blueprintjs/core";
import {ClassImageView} from "./classImageView";

export interface ClassFileWindowProps{
    mosaicPath: MosaicPath,
    classFiles: ClassFile[]
    selectedLines?: number[]
    selectedFile?: string
}

export const ClassFileWindow = (props: ClassFileWindowProps) => {
    const [classFileIndex, setClassFileIndex] = useState(0)
    const handleTabChange = (i: number) => {
        setClassFileIndex(i)
    }


    return <MosaicWindow<string>
        path={props.mosaicPath}
        title="Output"
        renderToolbar={() => {
            return <div style={{width: "100%"}}>
                <Tabs id="class-file-tabs"
                      onChange={handleTabChange}
                      selectedTabId={classFileIndex}
                >
                    {props.classFiles && props.classFiles.map((classFile, i) => <Tab key={i + ""} id={i} title={classFile.path} />)}
                </Tabs>
            </div>
        }}
    >
        {
            props.classFiles[classFileIndex] && <div className="class-image-window-content">
                {
                    // TODO handle file name
                }
                <ClassImageView selectedLines={props.selectedLines} classImage={props.classFiles[classFileIndex].classImage} />
            </div>
        }

    </MosaicWindow>
}
import './outputWindow.css'
import React, {useState} from "react";
import {MosaicPath, MosaicWindow} from "react-mosaic-component";
import {Button, ButtonGroup} from "@blueprintjs/core";
import {TitleBar} from "../titleBar/titleBar";

export type OutputType = "stdout" | "stderr"

export interface OutputMsg{
    type: OutputType
    msg: string
}

export interface OutputWindowProps{
    mosaicPath: MosaicPath
    outputMsgs: OutputMsg[]
}
export const OutputWindow = (props: OutputWindowProps) => {
    const [compilerIndex, setCompilerIndex] = useState(0)
    const toolbar = <div style={{width: "100%"}}>
        <TitleBar title={"Output"} actions={<ButtonGroup minimal={true}>
            <Button icon="trash" />
        </ButtonGroup>} />
    </div>
    return <MosaicWindow<string>
        path={props.mosaicPath}
        title={`Output`}
        renderToolbar={() => toolbar}
    >
        <div className="output-window-content">

        </div>
    </MosaicWindow>
}
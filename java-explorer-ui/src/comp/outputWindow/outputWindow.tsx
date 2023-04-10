import './outputWindow.css'
import React from "react";
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
    onClearMsg?: () => void
}
export const OutputWindow = (props: OutputWindowProps) => {
    const toolbar = <div style={{width: "100%"}}>
        <TitleBar title={"Output"} actions={<ButtonGroup minimal={true}>
            <Button onClick={props.onClearMsg} icon="trash" />
        </ButtonGroup>} />
    </div>
    const msgRow = (msg: OutputMsg, i: number) => <div key={i + ''} className={`output-msg-${msg.type}`}>{msg.msg}</div>
    return <MosaicWindow<string>
        path={props.mosaicPath}
        title={`Output`}
        renderToolbar={() => toolbar}
    >
        <div className="output-window-content">
            <pre>{
                props.outputMsgs.map(msgRow)
            }
            </pre>
        </div>
    </MosaicWindow>
}
import './OutputWindow.css'
import React from "react";
import {Button, ButtonGroup} from "@blueprintjs/core";
import {TitleBar} from "../titleBar/TitleBar";
import {AppWindow} from "../window/AppWindow";


export type OutputType = "stdout" | "stderr"

export interface OutputMsg{
    type: OutputType
    msg: string
}

export interface OutputWindowProps{
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

    return<AppWindow title={"Output"} actions={toolbar} >

        <div className="output-window-content">
            <pre>{
                props.outputMsgs.map(msgRow)
            }
            </pre>
        </div>
    </AppWindow>
}
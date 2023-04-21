import './OutputWindow.css'
import React, {UIEventHandler, useEffect, useRef, useState} from "react";
import {Button, ButtonGroup} from "@blueprintjs/core";
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
    const [autoscroll, setAutoscroll] = useState(false)
    const divRef = useRef(null as HTMLDivElement | null)
    const msgRow = (msg: OutputMsg, i: number) => <div key={i + ''} className={`output-msg output-msg-${msg.type}`}>{msg.msg}</div>

    useEffect(() => {
        const div = divRef.current
        if(div && autoscroll){
            div.scrollTop = div.scrollHeight
        }
    }, [props.outputMsgs])

    const handleScroll: UIEventHandler<HTMLDivElement> = (e) => {
        const div = divRef.current
        if(!div){
            return
        }
        const bottom = Math.abs(div.scrollHeight - div.scrollTop - div.clientHeight) < 1
        setAutoscroll(bottom)
    }
    return <AppWindow title={"Output"} actions={<ButtonGroup minimal={true}>
        <Button onClick={props.onClearMsg} icon="trash" />
    </ButtonGroup>} >

        <div onScroll={handleScroll} ref={divRef} className="output-window-content">
            <pre>{
                props.outputMsgs.map(msgRow)
            }
            </pre>
        </div>
    </AppWindow>
}
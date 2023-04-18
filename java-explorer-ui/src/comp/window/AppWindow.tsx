import {ReactElement, ReactNode} from "react";
import {TitleBar} from "../titleBar/TitleBar";
import './AppWindow.css'

export interface WindowProps{
    children?: ReactNode
    title: ReactNode
    actions: ReactElement
}
export function AppWindow(props: WindowProps){
    return <div className="app-window">
        <div className="app-window-title">
            <TitleBar title={props.title} actions={props.actions} />
        </div>
        <div className="app-window-body">
            {props.children}
        </div>
    </div>
}
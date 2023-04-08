import {ReactNode} from "react";
import './titleBar.css'

export interface TitleBarProps{
    title: ReactNode
    actions: JSX.Element
}

export const TitleBar = (props: TitleBarProps) => {
    return <div className="title-bar">
        <div className="title-bar-title">
            {props.title}
        </div>
        <div  className="title-bar-actions">
            {props.actions}
        </div>
    </div>
}
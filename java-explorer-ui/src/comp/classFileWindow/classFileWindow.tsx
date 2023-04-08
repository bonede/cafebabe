import './classFileWindow.css'
import {MosaicPath, MosaicWindow} from "react-mosaic-component";
import React from "react";
import {ClassImage} from "../../api/ApiClient";
import {Tab, Tabs} from "@blueprintjs/core";

export interface ClassFileWindowProps{
    mosaicPath: MosaicPath,
    classImages: ClassImage[]
}
export const ClassFileWindow = (props: ClassFileWindowProps) => {
    return <MosaicWindow<string>
        path={props.mosaicPath}
        title={`Output`}
        renderToolbar={() => {
            return <div style={{width: "100%"}}><Tabs id="class-file-tabs" selectedTabId="rx">
                {props.classImages.map(i => <Tab id={i.className} title={i.className} />)}
            </Tabs></div>
        }}
    >
        <div className="output-window-content">

        </div>
    </MosaicWindow>
}
import './app.css'
import React, {useEffect, useState} from "react";
import {ApiClient, AppInfo, ClassImage} from "../../api/ApiClient";


import {Mosaic, MosaicPath} from 'react-mosaic-component';
import {ClassFileWindow} from "../classFileWindow/classFileWindow";
import {OutputMsg, OutputWindow} from "../outputWindow/outputWindow";
import {EditorWindow} from "../editorWindow/editorWindow";


type WindowType = 'editor' | 'output' | 'classFile'



export const JavaExplorerApp = () => {
    const apiClient = ApiClient.getClient()
    const [outputMsgs, setOutputMsg] = useState([] as OutputMsg[])
    const [classImages, setClassImages] = useState([] as ClassImage[])
    const [appInfo, setAppInfo] = useState(null as AppInfo | null)
    useEffect(() => {
        apiClient.getAppInfo().then(a => setAppInfo(a))
    }, [])
    const windowForId = (id: WindowType, path: MosaicPath) => {
        switch (id){
            case "classFile": return <ClassFileWindow mosaicPath={path} classImages={classImages}  />
            case "output": return <OutputWindow mosaicPath={path} outputMsgs={outputMsgs}  />
            case "editor": return <EditorWindow mosaicPath={path} compilers={appInfo!.compilers} />
        }
    }
    return <div id="app">
        {
            appInfo ? <Mosaic<WindowType>
                className="mosaic-blueprint-theme bp4-dark"
                blueprintNamespace="bp4"
                renderTile={windowForId}
                initialValue={{
                    direction: 'row',
                    first: {
                        direction: 'column',
                        first: 'editor',
                        second: 'output',
                        splitPercentage: 80,
                    },
                    second: 'classFile',
                    splitPercentage: 50,
                }}
            /> : null
        }
    </div>
}
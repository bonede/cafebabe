import {MosaicPath, MosaicWindow} from "react-mosaic-component"
import React, {useState} from "react"
import {CompilerInfo} from "../../api/ApiClient"
import {Editor} from './editor'
import {Button, ButtonGroup, MenuItem} from "@blueprintjs/core"
import {TitleBar} from "../titleBar/titleBar";
import {ItemRenderer, Select2} from "@blueprintjs/select";

export interface EditorWindowProps{
    mosaicPath: MosaicPath,
    compilers: CompilerInfo[]
}
const renderFilm: ItemRenderer<CompilerInfo> = (compilerInfo, { handleClick, handleFocus, modifiers, query }) => {
    if (!modifiers.matchesPredicate) {
        return null;
    }
    return (
        <MenuItem
            active={modifiers.active}
            disabled={modifiers.disabled}
            key={compilerInfo.lang}
            text={compilerInfo.name}
            onClick={handleClick}
            onFocus={handleFocus}
            roleStructure="listoption"
            label={`${compilerInfo.lang}`}
        />
    );
};
export const EditorWindow = (props: EditorWindowProps) => {
    const [compilerInfo, setCompilerInfo] = useState(props.compilers[0])
    const toolbar = <div style={{width: "100%"}}>
        <TitleBar title={compilerInfo.fileName} actions={
            <div style={{display: "flex"}}>
                <Select2<CompilerInfo>
                    filterable={false}
                    items={props.compilers}
                    onItemSelect={(i) => setCompilerInfo(i)}
                    itemRenderer={renderFilm}
                >
                    <Button minimal={true} text={compilerInfo.name} rightIcon="double-caret-vertical" placeholder="Select a compiler" />
                </Select2>
                <ButtonGroup minimal={true}>
                    <Button icon="build" />
                    <Button icon="link" />
                </ButtonGroup>
            </div>
        } />
    </div>
    return <MosaicWindow<string>
        path={props.mosaicPath}
        title={`Editor`}
        renderToolbar={() => toolbar}
    >
            <Editor
                lang={compilerInfo.lang}
                content={compilerInfo.example}
            />
    </MosaicWindow>
}
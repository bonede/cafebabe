import './editor.css'
import * as monaco from 'monaco-editor';
import {useEffect, useRef} from "react";

export default function Editor(){

    const editorElement = useRef(null);
    useEffect(() => {
        monaco.editor.create(editorElement.current!, {
            theme: "vs-dark",
            value: ['function x() {', '\tconsole.log("Hello world!");', '}'].join('\n'),
            language: 'javascript',
            automaticLayout: true
        })
    });

    return <div ref={editorElement} className="editor"></div>
}
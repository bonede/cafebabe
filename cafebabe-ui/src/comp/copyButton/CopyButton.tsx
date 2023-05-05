import {Button} from "@blueprintjs/core";
import React, {useState} from "react";
import {Popover2} from "@blueprintjs/popover2";

export interface CopyButtonProps{
    content?: string
}
export function CopyButton(props: CopyButtonProps){
    const [copied, setCopied] = useState(false)
    const handleClick = async () => {
        if (!props.content) {
            return
        }
        try {
            await navigator.clipboard.writeText(props.content)
            setCopied(true)
            setTimeout(() => {
                setCopied(false)
            }, 1 * 1000)
        } catch (e) {
            setCopied(false)
        }

    }
    return <span>
        <Popover2 onInteraction={state => setCopied(state)}  isOpen={copied} shouldReturnFocusOnClose={false} position="bottom" content={
            <div style={{padding: "10px"}}>
                {copied ? "Copied" : ""}
            </div>
        }>
            <Button onClick={handleClick} icon="duplicate" minimal={true} />
        </Popover2>
    </span>
}
import {Button, Dialog, DialogBody, DialogFooter} from "@blueprintjs/core";
import {CopyButton} from "../copyButton/CopyButton";
import React from "react";
import {ShareResp} from "../../api/ApiClient";

export interface ShareRespDialogProps{
    shareResp?: ShareResp
    onClose?: () => void
}
export function ShareRespDialog(props: ShareRespDialogProps){

    return <Dialog onClose={props.onClose} isOpen={props.shareResp !== undefined} title="Share"  className="bp4-dark">
        <DialogBody>
            <div className="bp4-text-large">
                <p>Share Link</p>
                <p className="bp4-monospace-text"><a href={props.shareResp?.url} target="_blank">{props.shareResp?.url}</a>  <CopyButton content={props.shareResp?.url} /></p>
                <p>Deleting Link</p>
                <p className="bp4-monospace-text">{props.shareResp?.deletingUrl} <CopyButton content={props.shareResp?.deletingUrl} /></p>
            </div>
        </DialogBody>
        <DialogFooter actions={<Button intent="primary" text="Okay" onClick={props.onClose} />} />
    </Dialog>
}
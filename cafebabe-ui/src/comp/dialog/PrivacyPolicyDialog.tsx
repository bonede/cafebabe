import {Button, Dialog, DialogBody, DialogFooter} from "@blueprintjs/core";
import React from "react";

export interface PrivacyDialogProps {
    isOpen?: boolean
    onClose: () => void
}
export function PrivacyPolicyDialog(props: PrivacyDialogProps){

    return <Dialog onClose={props.onClose} isOpen={props.isOpen} title="Privacy Policy" className="bp4-dark">
        <DialogBody>
            <div className="bp4-text-large">
                <h3>Submitted source code</h3>
                <p>Submitted source code is stored in a temporary directory on server and deleted when compilation finished.</p>
                <h3>Share link</h3>
                <p>Source code in share link is stored on server and available to anyone has the link. It is deleted when self destruct time is up or deleted manually.</p>
                <h3>Tracking</h3>
                <p>We use Google Analytics to collect anonymous usage data to improve the product.</p>
            </div>
        </DialogBody>
        <DialogFooter actions={[<Button text="Close" onClick={props.onClose} />]} />
    </Dialog>
}
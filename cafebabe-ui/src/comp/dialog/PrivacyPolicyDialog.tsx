import {Button, Dialog, DialogBody, DialogFooter} from "@blueprintjs/core";
import React from "react";

export interface PrivacyDialogProps {
    isOpen?: boolean
    onClose: () => void
}
export function PrivacyPolicyDialog(props: PrivacyDialogProps){

    return <Dialog onClose={props.onClose} isOpen={props.isOpen} title="About Cafebabe" className="bp4-dark">
        <DialogBody>
            <div className="bp4-text-large">
                <h3>How Cafebabe works</h3>
                <p></p>
            </div>
        </DialogBody>
        <DialogFooter actions={[<Button text="Close" onClick={props.onClose} />]} />
    </Dialog>
}
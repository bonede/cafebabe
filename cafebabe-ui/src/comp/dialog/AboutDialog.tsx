import {Button, Dialog, DialogBody, DialogFooter} from "@blueprintjs/core";
import React from "react";

export interface AboutDialogProps {
    isOpen?: boolean
    onClose: () => void
}
export function AboutDialog(props: AboutDialogProps){

    return <Dialog onClose={props.onClose} isOpen={props.isOpen} title="About Cafebabe" className="bp4-dark">
        <DialogBody>
            <div className="bp4-text-large">
                <h3>Cafebabe v0.0.1</h3>
                <p>Project home</p>
                <p><a href="https://github.com/bonede/cafebabe">https://github.com/bonede/cafebabe</a> </p>
            </div>
        </DialogBody>
        <DialogFooter actions={[<Button text="Close" onClick={props.onClose} />]} />
    </Dialog>
}
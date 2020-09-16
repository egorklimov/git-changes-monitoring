import React from "react";
import IconButton from "@material-ui/core/IconButton";
import Dialog from "@material-ui/core/Dialog";
import DialogTitle from "@material-ui/core/DialogTitle";
import DialogContent from "@material-ui/core/DialogContent";
import DialogContentText from "@material-ui/core/DialogContentText";
import TextField from "@material-ui/core/TextField";
import Container from "@material-ui/core/Container";
import {Typography} from "@material-ui/core";
import DialogActions from "@material-ui/core/DialogActions";
import Button from "@material-ui/core/Button";
import FormSubmitButton from "./organization/FormSubmitButton";
import LinkIcon from '@material-ui/icons/Link';

export default function AddRepositoryDialog() {
    const [open, setOpen] = React.useState(false);
    const [organization, setOrganization] = React.useState();
    const [currentRepository, setCurrentRepository] = React.useState();

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    const handleSubmit = () => {
        setOpen(false);
        console.log(organization);
    }

    const handleChange = (e) => {
        e.preventDefault();
        setOrganization(e.target.value);
    }

    const handleLoad = (repository, description) => {
        setCurrentRepository({url: repository, description: description});
    }
    return (
        <div>
            <IconButton onClick={handleClickOpen}>
                <LinkIcon />
            </IconButton>
            <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
                <DialogTitle id="form-dialog-title">Github repository</DialogTitle>
                <DialogContent>
                    <DialogContentText>
                        To add a public repository from Github, please, enter the link to the repository.
                        Name is case sensitive, and must end with .git.
                        <br/>
                        It may take a while to get the repository, please do not cancel the download.
                    </DialogContentText>
                    <TextField
                        autoComplete="off"
                        autoFocus
                        margin="dense"
                        id="name"
                        placeholder="https://github.com/egorklimov/git-changes-monitoring.git"
                        label="Repository link"
                        fullWidth
                        onChange={handleChange}
                    />

                    {currentRepository &&
                    <Container maxWidth="sm">
                        <Typography>
                            {currentRepository}
                        </Typography>
                    </Container>
                    }
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose} color="primary">
                        Cancel
                    </Button>
                    <FormSubmitButton onClick={handleSubmit} org={organization} onLoad={handleLoad} />
                </DialogActions>
            </Dialog>
        </div>
    );
}
<template>
    <v-container class="d-flex align-center justify-center">
        <div style="width: 925px;">
            <button @click="back">
            <v-icon size="40">mdi-arrow-left</v-icon>
            </button>
            <div class="title">New User</div>
            <div class="subtitle">Create a new User</div>
            <v-form @submit-prevent="addUser">
                <v-row justify-md="strech">
                    <v-col>
                        <label for="">Firstname*</label>
                        <InputField class="inputfield" required
                        :model-value="user.firstname" 
                        @update:model-value="newValue => user.firstname = newValue" inputType="text" />
                    </v-col>
                    <v-col>
                        <label for="">Lastname*</label>
                        <InputField class="inputfield"
                        :model-value="user.lastname" required
                        @update:model-value="newValue => user.lastname = newValue" inputType="text" />
                    </v-col>
                </v-row>

                <v-row>
                    <v-col>
                        <label for="">E-Mail-Address*</label>
                        <InputField class="inputfield" required
                        :model-value="user.email" 
                        @update:model-value="newValue => user.email = newValue" inputType="text" />
                    </v-col>
                    <v-col>
                        <label for="">Phone-Number</label>
                        <InputField class="inputfield" :model-value="user.phoneNumber" 
                        @update:model-value="newValue => user.phoneNumber = newValue"></InputField>
                    </v-col>
                </v-row>

                <v-row>
                    <v-col>
                        <label for="">Roles*</label>
                        <v-select
                            :items="roles"
                            item-title="name"
                            item-value="id"
                            v-model="selectedRoles"
                            bg-color="none"
                            density="compact"
                            variant="plain"
                            multiple
                            class="select"
                        >
                            <template v-slot:selection="{item, index}">
                                <v-chip v-if="index < 2">
                                    <span>{{ item.title }}</span>
                                </v-chip>
                                <span 
                                v-if="index === 2"
                                class="text-grey text-caption align-self-center"
                                >
                                    (+{{ selectedRoles.length - 2 }} others)
                                </span>
                            </template>
                        </v-select>
                    </v-col>
                    <v-col>
                       <DatePicker @update-date="handleDateUpdate" />
                    </v-col>
                </v-row>
                <v-row>
                    <v-col>
                        <label for="">Password*</label>
                        <InputField class="inputfield" 
                            required
                            :model-value="password" 
                            @update:model-value="newValue => password = newValue" inputType="secure"/>
                    </v-col>
                    <v-col>
                        <label for="">Confirm Password*</label>
                        <InputField class="inputfield"
                            :model-value="confirmpassword" required
                            @update:model-value="newValue => confirmpassword = newValue" inputType="secure"/>
                    </v-col>
                </v-row>
                <div v-if="invalidInput" class="error-message">{{ invalidInput }}</div>
                <v-row>
                    <v-col>
                        <div class="btn-div">
                            <Btn @click="addUser" label="Create" />
                        </div>
                    </v-col>
                </v-row>
            </v-form>
        </div>
    </v-container>
</template>

<script setup>
    import InputField from '@/components/Input.vue';
    import Btn from '@/components/Button.vue';
    import DatePicker from '@/components/DatePicker.vue';
    import axios from 'axios';
    import LoginView from './LoginView.vue';
</script>
  
<script>
    export default {
        components: {
            InputField,
            DatePicker,
            Btn,
            LoginView,
        },
        data() {
            return {
                user: {},
                confirmpassword: '',
                password: '',
                invalidInput: '',
                roles: [],
                selectedRoles: [],
                countries: [],
                dial_code: 0,
                dob: new Date(),
            }
        },
        async mounted() {
            try {
                const response = await axios.get('/roles/all');
                for(const r of response.data) 
                {
                    let name = r.name.charAt(0) + r.name.slice(1).toLowerCase()
                    name = name.replace("_w", " W");
                    const role = {id: r.id, name: name};
                    this.roles.push(role);
                }
            } catch (error) {
                alert('An error occurred: ', error)
            }
        },
        methods: {
            back() {
                this.$router.push({ name: 'Home' });
            },
            async addUser() {
                this.invalidInput = '';
                let user = this.user;

                try {
                    if(user.firstname == null ||user.firstname == '' || user.lastname == null || user.lastname == '' ||
                       user.email == null || user.email == '' || this.selectedRoles.length == 0 || this.password == null || this.password == '' || 
                       this.confirmpassword == null || this.confirmpassword == '') {
                        this.invalidInput = 'Please fill out all required fields.';

                    } else if(!(/^[a-z.-]+@[a-z.-]+\.[a-z]+$/i.test(user.email))) {
                        this.invalidInput = 'Invalid e-mail.'

                    } else if(this.password != this.confirmpassword) {
                        this.invalidInput = 'Password mismatch.';
                    } else if(!(/^\d{7,15}$/.test(user.phoneNumber)))
                        this.invalidInput = 'Invalide phone-number'
                    else {
                        const formData = new URLSearchParams();

                        formData.append('firstname', user.firstname.charAt(0).toUpperCase() + user.firstname.slice(1));
                        formData.append('lastname', user.lastname.charAt(0).toUpperCase() + user.lastname.slice(1));
                        formData.append('email', user.email);
                        formData.append('passwordHash', this.password);
                        formData.append('phoneNumber', user.phoneNumber);

                        if(this.dob != new Date()) 
                            formData.append('birthdate', this.dob);
                        console.log(this.selectedRoles)
                        formData.append('roles', this.selectedRoles);
                        const priviledgeValue = this.selectedRoles.includes(1) ? [1] : [2];
                        formData.append('priviledgeRoles', priviledgeValue);

                        const response = await axios.post('users/add', formData, {
                            withCredentials: true
                        });

                        if (response.status === 200 && response.data) {
                            this.$router.push({name: 'Home'});
                        } else {
                           alert('An error occurred, pleaes try again.');
                        }
                    }
                } catch (error) {
                   alert(error)
                }
            },
            handleDateUpdate(newDate) {
                this.dob = newDate;
            }
        }
    };
</script>

<style scoped>
    .title {
        margin-top: 10px;
        color: black;
        font-size: 30px;
    }

    .subtitle {
        color: black;
        font-size: 20px;
        margin-top: -10px;
        margin-bottom: 40px;
    }

    .btn-div {
        display: flex;
        justify-content: end;
    }

    .select {
        border: #707070 1px solid;
        background: white !important;
        height: 50px;
        margin: 10px 0px;
        min-width: 300px;
        border-radius: 7px;
        padding-inline: 10px;
        padding-top: 5px;
        display: block;
    }

    .error-message {
        color: red;
        font-size: 12px;
        margin-top: 5px;
    }
</style>

  
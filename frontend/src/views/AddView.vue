<template>
    <v-container class="outer">
        <div class="inner">
            <button @click="back">
            <v-icon size="40">mdi-arrow-left</v-icon>
            </button>
            <div class="title">New User</div>
            <div class="subtitle">Create a new User</div>
            <v-form @submit-prevent="addUser">
                <v-row>
                    <v-col>
                        <label for="">Firstname*</label>
                        <InputField class="inputfield" required
                        :model-value="user.firstname" 
                        @update:model-value="newValue => user.firstname = newValue" inputType="text" />
                    </v-col>
                    <v-col>
                        <label for="">Lastname*</label>
                        <InputField
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
                        <InputField></InputField>
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
                        <label for="">Date of Birth</label>
                        <v-row justify="start">
                            <v-col>
                                <v-select
                                    :items="months"
                                    v-model="dob.month"
                                    bg-color="none"   
                                    density="compact"
                                    variant="plain"
                                    class="select_date"
                                ></v-select>
                            </v-col>
                            <v-col>
                                <v-select
                                :items="days"
                                v-model="dob.day"
                                bg-color="none"
                                density="compact"
                                variant="plain"
                                class="select_date year"
                                ></v-select>
                            </v-col>
                            <v-col>
                                <v-select
                                :items="years"
                                v-model="dob.year"
                                bg-color="none"
                                density="compact"
                                variant="plain"
                                class="select_date year"
                                ></v-select>
                            </v-col>
                        </v-row>
                    </v-col>
                </v-row>

                <v-row>
                    <v-col>
                        <label for="">Password*</label>
                        <InputField class="inputfield" 
                            width="400px" required
                            :model-value="password" 
                            @update:model-value="newValue => password = newValue" inputType="secure"/>
                    </v-col>
                    <v-col>
                        <label for="">Confirm Password*</label>
                        <InputField 
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
  
<script>
    import InputField from '@/components/Input.vue';
    import Btn from '@/components/Button.vue';
    import axios from 'axios';
    import LoginView from './LoginView.vue';
import { setTransitionHooks } from 'vue';

    export default {
        data() {
            return {
                user: {},
                confirmpassword: '',
                password: '',
                invalidInput: '',
                roles: [],
                selectedRoles: [],
                dob: {
                    month: 'Month',
                    day: 'Day',
                    year: 'Year'
                },
                months: [
                    'January', 'February', 'March', 'April', 'May', 'June',
                    'July', 'August', 'September', 'October', 'November', 'December'
                ],
                years: (() => {
                    const currentYear = new Date().getFullYear();
                    const years = [];
                    for (let i = currentYear; i >= currentYear - 100; i--) {
                    years.push(i);
                    }
                    return years;
                })(),
                days: Array.from({ length: 31 }, (_, i) => i + 1),
            }
        },
        components: {
            InputField,
            Btn,
        },
        async mounted() {
            try {
                const response = await axios.get('roles/all');
                for(const r of response.data) 
                {
                    let name = r.name.charAt(0) + r.name.slice(1).toLowerCase()
                    name = name.replace("_w", " W");
                    const role = {id: r.id, name: name};
                    this.roles.push(role);
                }
            } catch (error) {
                console.log('An error occurred: ', error)
            }
        },
        methods: {
            back() {
                this.$router.push({ name: 'Home' });
            },
            async addUser() {
                this.invalidInput = '';
                console.log(this.selectedRoles)
                let user = this.user;
                try {
                    if(user.firstname == null ||user.firstname == '' ||
                       user.lastname == null || user.lastname == '' ||
                       user.email == null || user.email == '' ||
                       this.selectedRoles.length == 0 || 
                       this.password == null || this.password == '' || 
                       this.confirmpassword == null || this.confirmpassword == '') {
                        this.invalidInput = 'Please fill out all required fields.';
                    } else if(!(/^[a-z.-]+@[a-z.-]+\.[a-z]+$/i.test(user.email))) {
                        this.invalidInput = 'Invalid e-mail.'
                    } else if(this.password != this.confirmpassword) {
                        this.invalidInput = 'Password mismatch.';
                    } else {
                        const formData = new URLSearchParams();
                        formData.append('firstname', user.firstname.charAt(0).toUpperCase() + user.firstname.slice(1));
                        formData.append('lastname', user.lastname.charAt(0).toUpperCase() + user.lastname.slice(1));
                        formData.append('email', user.email);
                        formData.append('password', this.password);
                        formData.append('number', BigInt(0));
                        if(!(this.dob.year == 'Year' || this.dob.date == 'Day' || this.dob.month == 'Month')) 
                            formData.append('birthdate', new Date(this.dob.year, this.months.indexOf(this.dob.month), this.dob.day));
                        formData.append('roles', this.selectedRoles);

                        const response = await axios.post('users/add', formData);

                        if (response.status === 200 && response.data) {
                            this.$router.push({name: 'Home'});
                        } else {
                            console.error('Error adding user');
                        }
                    }
                } catch (error) {
                    console.error('An error occurred:', error);
                }
            }
        }
    };
</script>

<style scoped>
    .inner {
       display: inline-block;
       margin: auto;
    }

    .outer{
        display: flex;
        align-items: center;
    }

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

    .col-end {
        margin-left: auto;
    }

    .btn-div {
        display: flex;
        justify-content: end;
    }

    .inputfield {
        margin-right: 20px;
    }

    .select_date {
        border: solid 1px #707070;
        border-radius: 7px;
        margin-top: 10px;
        height: 50px;
        padding-inline: 10px;
        width: 150px;
        padding-top: 5px;
    }

    .year {
        width: 100px;
    }

    .select {
        border: #707070 1px solid;
        background: white !important;
        height: 50px;
        width: 400px;
        margin: 10px 0px;
        margin-right: 20px;
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

  
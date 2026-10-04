package com.example.data.repository

import com.example.data.local.dao.CalendarDao
import com.example.data.local.dao.ProjectDao
import com.example.data.local.entity.CalendarEntity
import com.example.data.local.entity.ProjectEntity
import com.example.domain.model.CalendarEntry
import com.example.domain.model.ContentStatus
import com.example.domain.model.ContentType
import com.example.domain.model.CreatorProject
import com.example.domain.model.Platform
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ProjectRepository(private val projectDao: ProjectDao) {

    val allProjects: Flow<List<CreatorProject>> = projectDao.getAllProjects().map { list ->
        list.map { it.toDomain() }
    }

    val totalCount: Flow<Int> = projectDao.getProjectCount()
    val scriptCount: Flow<Int> = projectDao.getScriptCount()
    val ideaCount: Flow<Int> = projectDao.getIdeaCount()

    fun getProjectsByType(type: ContentType): Flow<List<CreatorProject>> {
        return projectDao.getProjectsByType(type.name).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun getProjectById(id: Long): CreatorProject? {
        return projectDao.getProjectById(id)?.toDomain()
    }

    suspend fun insertProject(project: CreatorProject): Long {
        return projectDao.insertProject(ProjectEntity.fromDomain(project))
    }

    suspend fun updateProject(project: CreatorProject) {
        projectDao.updateProject(ProjectEntity.fromDomain(project))
    }

    suspend fun deleteProjectById(id: Long) {
        projectDao.deleteProjectById(id)
    }

    suspend fun deleteAll() {
        projectDao.deleteAll()
    }

    suspend fun initializeDemoProjects() {
        val currentCount = projectDao.getProjectCount().first()
        if (currentCount == 0) {
            val demoList = listOf(
                CreatorProject(
                    title = "5 idées de vidéos business à lancer avec 50.000 FCFA",
                    type = ContentType.VIDEO_IDEA,
                    dateEpoch = System.currentTimeMillis() - 86400000L * 2,
                    preview = "1. Vente d'accessoires téléphones à la fac\n2. Service de livraison express de repas locaux\n3. Gestion de réseaux sociaux pour maquis...",
                    content = """
                        # 5 Idées de Business Rentables en Afrique (Moins de 50.000 FCFA)
                        
                        1. **Vente d'accessoires de smartphones (écouteurs, coques, câbles)**
                           - Cible : Étudiants et jeunes professionnels.
                           - Marge moyenne : 40% à 60%.
                           - Conseil contenu : Montrer la durabilité des câbles face aux faux sur le marché.
                        
                        2. **Box apéritif / Snacking local gourmet**
                           - Produits : Alloco chips épicées, arachides caramélisées, jus de bissap frais en bouteilles stylées.
                           - Cible : Bureaux d'entreprises à l'heure de la pause.
                        
                        3. **Community Management pour boutiques de quartier**
                           - Créer 3 Reels / TikTok par semaine pour les salons de coiffure ou restaurants.
                           - Investissement : Juste ton smartphone et ta créativité.
                        
                        4. **Friperie sélective (Thrifting chic)**
                           - Chiner les meilleures pièces au marché (ex: Adjamé, Dantokpa, Colobane).
                           - Nettoyer, repasser, shooter en vidéo esthétique avec modèle.
                        
                        5. **Formations courtes WhatsApp sur les outils digitaux**
                           - Canva, montage CapCut, gestion de budget Excel mobile.
                    """.trimIndent(),
                    platform = Platform.TIKTOK,
                    country = "Côte d'Ivoire",
                    isFavorite = true,
                    tags = "Business, Étudiants, 50k"
                ),
                CreatorProject(
                    title = "Histoire d'un jeune entrepreneur : Du zéro au premier million",
                    type = ContentType.STORY,
                    dateEpoch = System.currentTimeMillis() - 86400000L,
                    preview = "Koffi n'avait qu'un vieux téléphone et 10.000 FCFA. Voici comment il a bâti sa marque de streetwear africain...",
                    content = """
                        # Titre : Du gbê au sommet : L'histoire inspirante de Koffi
                        
                        ## Personnages
                        - **Koffi (23 ans)** : Passionné de mode, persévérant, débrouillard.
                        - **Maman Awa** : Sa mère qui lui prête son fer à repasser et ses encouragements.
                        
                        ## Contexte
                        Yopougon, Abidjan. Diplômé sans emploi, Koffi refuse de baisser les bras malgré la pression familiale.
                        
                        ## Début
                        Avec 10.000 FCFA empruntés, il achète 5 t-shirts blancs unis au grand marché et collabore avec un petit sérigraphe du quartier pour imprimer des proverbes africains en typographie moderne.
                        
                        ## Développement
                        Il poste sa première vidéo TikTok sans budget : il montre la confection artisanale en toute transparence. Pendant 2 semaines, zéro vente. Il doute. Puis une créatrice connue partage sa vidéo.
                        
                        ## Climax
                        En une nuit, il reçoit 150 commandes par WhatsApp. Son téléphone surchauffe, il n'a pas le stock. Au lieu de paniquer, il fait un live pour expliquer avec sincérité la rupture et promet de livrer en 48h. L'audience adore son honnêteté.
                        
                        ## Fin
                        Aujourd'hui, sa marque emploie 4 tailleurs locaux et expédie dans toute la sous-région.
                        
                        ## Morale
                        La transparence et la persévérance valent plus qu'un gros budget marketing au départ.
                    """.trimIndent(),
                    platform = Platform.INSTAGRAM,
                    country = "Côte d'Ivoire",
                    isFavorite = true,
                    tags = "Storytelling, Motivation"
                ),
                CreatorProject(
                    title = "Publicité TikTok pour boutique de mode africaine moderne",
                    type = ContentType.AD_MAKER,
                    dateEpoch = System.currentTimeMillis() - 3600000L * 5,
                    preview = "Arrête de porter les mêmes chemises que tout le monde aux mariages ce week-end ! Découvre notre collection...",
                    content = """
                        # Publicité : Boutique AfrikChic
                        
                        🎯 **Hook Vidéo (0-3s)** :
                        [Visuel : Zoom sur un tissu wax brodé haute couture avec mouvement dynamique]
                        Voix off : « Arrête d'acheter les mêmes ensembles que tout le monde pour tes événements ce mois-ci ! »
                        
                        📝 **Script (4-20s)** :
                        « Chez AfrikChic à Treichville, chaque pièce est taillée sur-mesure dans nos ateliers avec des cotonnades 100% locales et des coupes contemporaines qui tombent impeccablement.
                        Que ce soit pour le bureau ou une cérémonie, démarque-toi avec classe. »
                        
                        📱 **Texte à l'écran** :
                        - ✂️ Coupe sur-mesure & finitions premium
                        - 🚚 Livraison express à domicile partout à Abidjan & sous-région
                        - 💰 À partir de 18.000 FCFA
                        
                        ⚡ **Call to Action (20-25s)** :
                        « Clique sur le lien WhatsApp dans la bio pour commander ta taille avant rupture de stock ! »
                    """.trimIndent(),
                    platform = Platform.TIKTOK,
                    country = "Côte d'Ivoire",
                    isFavorite = false,
                    tags = "Publicité, Mode, Wax"
                )
            )
            projectDao.insertAll(demoList.map { ProjectEntity.fromDomain(it) })
        }
    }
}

class CalendarRepository(private val calendarDao: CalendarDao) {

    val allEntries: Flow<List<CalendarEntry>> = calendarDao.getAllEntries().map { list ->
        list.map { it.toDomain() }
    }

    val totalCount: Flow<Int> = calendarDao.getEntryCount()

    fun getEntriesForDate(dateIso: String): Flow<List<CalendarEntry>> {
        return calendarDao.getEntriesForDate(dateIso).map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun insertEntry(entry: CalendarEntry): Long {
        return calendarDao.insertEntry(CalendarEntity.fromDomain(entry))
    }

    suspend fun updateEntry(entry: CalendarEntry) {
        calendarDao.updateEntry(CalendarEntity.fromDomain(entry))
    }

    suspend fun deleteEntryById(id: Long) {
        calendarDao.deleteEntryById(id)
    }

    suspend fun deleteAll() {
        calendarDao.deleteAll()
    }

    suspend fun initializeDemoCalendar() {
        val count = calendarDao.getEntryCount().first()
        if (count == 0) {
            val todayIso = java.time.LocalDate.now().toString()
            val tomorrowIso = java.time.LocalDate.now().plusDays(1).toString()
            val nextWeekIso = java.time.LocalDate.now().plusDays(3).toString()

            val demoCalendar = listOf(
                CalendarEntry(
                    dateIso = todayIso,
                    timeStr = "18:00",
                    title = "Reel : 3 erreurs des créateurs débutants",
                    platform = Platform.INSTAGRAM,
                    contentType = ContentType.SCRIPT,
                    status = ContentStatus.READY,
                    notes = "Prêt à être tourné avec la lumière naturelle"
                ),
                CalendarEntry(
                    dateIso = tomorrowIso,
                    timeStr = "12:30",
                    title = "TikTok : Témoignage client business",
                    platform = Platform.TIKTOK,
                    contentType = ContentType.VIDEO_IDEA,
                    status = ContentStatus.TO_PREPARE,
                    notes = "Prendre les rushs dans la boutique"
                ),
                CalendarEntry(
                    dateIso = nextWeekIso,
                    timeStr = "20:00",
                    title = "YouTube : VLOG coulisses d'une marque locale",
                    platform = Platform.YOUTUBE,
                    contentType = ContentType.VIDEO_PROMPT,
                    status = ContentStatus.IDEA,
                    notes = "Format long 10-12 minutes"
                )
            )
            calendarDao.insertAll(demoCalendar.map { CalendarEntity.fromDomain(it) })
        }
    }
}

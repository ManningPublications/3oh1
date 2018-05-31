grails.gorm.default.mapping = {
    id generator: 'org.hibernate.id.enhanced.SequenceStyleGenerator', params: [prefer_sequence_per_entity: true, sequence_per_entity_suffix: "_id_seq"]
}